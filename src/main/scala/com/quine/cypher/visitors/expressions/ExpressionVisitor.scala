package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.Expression

object ExpressionVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_Expression(ctx: CypherParser.OC_ExpressionContext): Expression = {
    ctx.oC_OrExpression().accept(OrExpressionVisitor)
    //ctx.oC_OrExpression().oC_XorExpression(0).oC_AndExpression(0).oC_NotExpression(0).oC_ComparisonExpression().oC_PartialComparisonExpression(0).oC_StringListNullPredicateExpression().oC_AddOrSubtractExpression().oC_MultiplyDivideModuloExpression(0).

    //Expression.BinOp(Operator.Plus, Expression.BinOp(Operator.Dot, Expression.Ident(Symbol("s")), Expression.Ident(Symbol("x"))), Expression.BinOp(Operator.Dot, Expression.Ident(Symbol("d")), Expression.Ident(Symbol("x"))))
  }
}
