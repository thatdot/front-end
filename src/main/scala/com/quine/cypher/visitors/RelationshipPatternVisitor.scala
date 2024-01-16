package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.utils.CheatingNameGenerator
import com.quine.language.ast.Predicate

class RelationshipPatternVisitor(lhs: Symbol, rhs: Symbol) extends CypherBaseVisitor[Predicate] {
  override def visitOC_RelationshipPattern(ctx: CypherParser.OC_RelationshipPatternContext): Predicate = {
    val edgeName = CheatingNameGenerator.synthesizeIdent()

    if(ctx.oC_LeftArrowHead() == null) {
      Predicate.ExistsEdge(edgeName, Nil, lhs, rhs)
    } else if (ctx.oC_RightArrowHead() == null) {
      Predicate.ExistsEdge(edgeName, Nil, rhs, lhs)
    } else {
      throw new RuntimeException("Yikes!")
    }
  }
}
