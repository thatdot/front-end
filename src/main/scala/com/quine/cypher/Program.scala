package com.quine.cypher

import com.quine.cypher.builders.ParserBuilder
import com.quine.cypher.parsing.{CypherLexer, CypherParser}
import com.quine.cypher.phases.{BooleanExpressionRewriter, PredicateLogicRewriter}
import com.quine.cypher.visitors.QueryVisitor
import org.antlr.v4.runtime._
import com.quine.language.ast.Expression._
import com.quine.language.ast.Operator._
import com.quine.language.ast.Value._

object Program {

  val tq0 =
    """MATCH (s:Source)-[:edge]->(d:Dest) RETURN s.x + d.x""".stripMargin

  val tq1 =
    """MATCH (l) WHERE id(l) = $that.data.id
      |MATCH (v) WHERE id(v) = idFrom('verb', l.verb)
      |SET v.type = 'verb',
      |    v.verb = l.verb
      |CREATE (l)-[:verb]->(v)
      |""".stripMargin

  def main(args: Array[String]): Unit = {
    val test = BinOp(Or,Literal(False),BinOp(XOr,Literal(False),BinOp(And,Literal(True),BinOp(Plus,BinOp(Dot,Ident('s),Ident('x)),BinOp(Dot,Ident('d),Ident('x))))))
    println(BooleanExpressionRewriter.rewrite(test))

    val parser = ParserBuilder.mkParser(List(PredicateLogicRewriter, BooleanExpressionRewriter))

    println(parser.parseCypher(tq1))
  }
}