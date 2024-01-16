package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_MatchContext
import com.quine.language.ast.Predicate

object MatchClauseVisitor extends CypherBaseVisitor[Predicate] {
  override def visitOC_Match(ctx: OC_MatchContext): Predicate = {
    val where = if(ctx.oC_Where() == null) {
      Predicate.True
    } else {
      ctx.oC_Where().accept(WhereClauseVisitor)
    }
    Predicate.And(
      lhs = ctx.oC_Pattern().accept(MatchPatternVisitor),
      rhs = where
    )
  }
}
