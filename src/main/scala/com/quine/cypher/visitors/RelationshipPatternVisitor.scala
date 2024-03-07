package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.utils.CheatingNameGenerator
import com.quine.language.ast.{Predicate, Source}

class RelationshipPatternVisitor(lhs: Symbol, rhs: Symbol) extends CypherBaseVisitor[Predicate] {
  override def visitOC_RelationshipPattern(ctx: CypherParser.OC_RelationshipPatternContext): Predicate = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )

    val edgeName = CheatingNameGenerator.synthesizeIdent()

    if(ctx.oC_LeftArrowHead() == null) {
      Predicate.ExistsEdge(src, edgeName, Nil, lhs, rhs)
    } else if (ctx.oC_RightArrowHead() == null) {
      Predicate.ExistsEdge(src, edgeName, Nil, rhs, lhs)
    } else {
      throw new RuntimeException("Yikes!")
    }
  }
}
