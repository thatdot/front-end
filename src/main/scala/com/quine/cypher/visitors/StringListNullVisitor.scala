package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.expressions.AddSubtractVisitor
import com.quine.language.ast.Expression

object StringListNullVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_StringListNullPredicateExpression(ctx: CypherParser.OC_StringListNullPredicateExpressionContext): Expression =
    ctx.oC_AddOrSubtractExpression().accept(AddSubtractVisitor)
}
