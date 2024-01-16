package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.Predicate

class PatternElementChainVisitor(lhs: Symbol) extends CypherBaseVisitor[Predicate] {
  override def visitOC_PatternElementChain(ctx: CypherParser.OC_PatternElementChainContext): Predicate = {
    val dest = ctx.oC_NodePattern().accept(NodePatternVisitor)

    val rhs = dest match {
      case Predicate.ExistsNode(binding, _) => binding
      case _ => throw new RuntimeException("This shouldn't happen.")
    }

    val rel = ctx.oC_RelationshipPattern().accept(new RelationshipPatternVisitor(lhs,rhs))

    Predicate.And(rel, dest)
  }
}
