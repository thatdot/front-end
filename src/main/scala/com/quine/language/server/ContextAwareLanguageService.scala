package com.quine.language.server

import com.quine.cypher.ParseError
import com.quine.cypher.builders.ParserBuilder
import com.quine.cypher.phases.{BooleanExpressionRewriter, PredicateLogicRewriter}

import java.util
import collection.JavaConverters._

class ContextAwareLanguageService {
  val edgeDictionary = Helpers.addItem("foo", Helpers.addItem("bar", SimpleTrie.Leaf))

  def edgeCompletions(startsWith: String): java.util.List[String] = {
    def go(xs: List[Char], level: SimpleTrie, prefix: String): List[String] = xs match {
      case h :: t => level match {
        case SimpleTrie.Node(children) => children.get(h) match {
          case Some(child) => go(t, child, prefix + h)
          case None => List() // No further path matches the prefix
        }
        case SimpleTrie.Leaf => List(prefix) // Found a leaf, return the current prefix
      }
      case Nil => level match {
        case SimpleTrie.Node(children) if children.nonEmpty =>
          children.flatMap { case (char, child) => go(Nil, child, prefix + char) }.toList
        case SimpleTrie.Node(_) => List(prefix) // If no children, return the prefix as a valid completion
        case SimpleTrie.Leaf => List(prefix) // Leaf reached, return the prefix
      }
    }

    go(startsWith.toList, edgeDictionary, "").asJava
  }

  def parseErrors(queryText: String): java.util.List[ParseError] = {
    val p = ParserBuilder.mkParser(List(PredicateLogicRewriter, BooleanExpressionRewriter))
    p.parseCypher(queryText) match {
      case Right(_) => new util.ArrayList[ParseError]()
      case Left(errs) => errs.asJava
    }
  }
}
