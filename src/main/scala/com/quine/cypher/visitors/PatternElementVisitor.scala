package com.quine.cypher.visitors

import cats.implicits._
import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_PatternElementContext
import com.quine.cypher.utils.Helpers.{maybeMatch, maybeMatchList}
import com.quine.language.ast.Predicate

import collection.JavaConverters._

object PatternElementVisitor extends CypherBaseVisitor[Predicate] {
  override def visitOC_PatternElement(ctx: OC_PatternElementContext): Predicate = {

    val r1: Option[Predicate] = maybeMatch(ctx.oC_NodePattern(), NodePatternVisitor)
    val r2 = maybeMatch(ctx.oC_PatternElement(), PatternElementVisitor)

    val leftName = r1 match {
      case Some(Predicate.ExistsNode(lhs, _)) => lhs
      case _ => throw new RuntimeException("This shouldn't happen!")
    }

    val r3 = maybeMatchList(ctx.oC_PatternElementChain(), new PatternElementChainVisitor(leftName)).map(chain => chain.foldRight(Predicate.sat)((chainPred, pred) => Predicate.And(pred, chainPred)))

    val lhs = (r1 <+> r2).get
    val rhs = r3.getOrElse(Predicate.sat)

    Predicate.And(lhs, rhs)
  }
}
