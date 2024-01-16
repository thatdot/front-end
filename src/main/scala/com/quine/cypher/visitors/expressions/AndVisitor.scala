package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.{Expression, Operator, Value}

import scala.collection.JavaConverters._

object AndVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_AndExpression(ctx: CypherParser.OC_AndExpressionContext): Expression =
    ctx.oC_NotExpression().asScala.toList.foldRight(Expression.mkLiteral(Value.True))((innerCtx, exp) => Expression.BinOp(Operator.And, exp, innerCtx.oC_ComparisonExpression().accept(ComparisonVisitor)))
}
