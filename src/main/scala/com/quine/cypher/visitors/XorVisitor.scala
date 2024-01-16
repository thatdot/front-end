package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.expressions.AndVisitor
import com.quine.language.ast.{Expression, Operator, Value}

import collection.JavaConverters._

object XorVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_XorExpression(ctx: CypherParser.OC_XorExpressionContext): Expression =
    ctx.oC_AndExpression().asScala.toList.foldRight(Expression.mkLiteral(Value.False))((innerCtx, exp) => Expression.BinOp(Operator.XOr, exp, innerCtx.accept(AndVisitor)))
}
