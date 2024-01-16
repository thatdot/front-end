package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_WhereContext
import com.quine.cypher.visitors.expressions.ExpressionVisitor
import com.quine.language.ast.Predicate

object WhereClauseVisitor extends CypherBaseVisitor[Predicate] {
  override def visitOC_Where(ctx: OC_WhereContext): Predicate = {
    val exp = ctx.oC_Expression().accept(ExpressionVisitor)
    Predicate.Satisfies(exp)
  }
}
