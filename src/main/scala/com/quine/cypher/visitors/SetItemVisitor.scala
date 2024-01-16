package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.expressions.{ExpressionVisitor, PropertyVisitor}
import com.quine.language.ast.Effect

object SetItemVisitor extends CypherBaseVisitor[Effect] {
  override def visitOC_SetItem(ctx: CypherParser.OC_SetItemContext): Effect = {
    val lhs = ctx.oC_PropertyExpression().accept(PropertyVisitor)
    val rhs = ctx.oC_Expression().accept(ExpressionVisitor)

    Effect.Set(lhs, rhs)
  }
}
