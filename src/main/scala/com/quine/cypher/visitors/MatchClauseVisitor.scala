package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_MatchContext
import com.quine.language.ast.{Predicate, Source}

object MatchClauseVisitor extends CypherBaseVisitor[Predicate] {
  override def visitOC_Match(ctx: OC_MatchContext): Predicate = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )

    val where = if(ctx.oC_Where() == null) {
      Predicate.True
    } else {
      ctx.oC_Where().accept(WhereClauseVisitor)
    }

    Predicate.And(
      source = src,
      lhs = ctx.oC_Pattern().accept(MatchPatternVisitor),
      rhs = where
    )
  }
}
