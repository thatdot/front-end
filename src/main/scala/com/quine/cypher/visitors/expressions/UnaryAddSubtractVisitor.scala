package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.Expression

object UnaryAddSubtractVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_UnaryAddOrSubtractExpression(ctx: CypherParser.OC_UnaryAddOrSubtractExpressionContext): Expression = {
    ctx.oC_NonArithmeticOperatorExpression().accept(NonArithmeticOperatorVisitor)
  }
}
