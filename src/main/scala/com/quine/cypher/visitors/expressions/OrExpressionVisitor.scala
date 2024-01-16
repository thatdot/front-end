package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.XorVisitor
import com.quine.language.ast.{Expression, Operator, Value}

import scala.collection.JavaConverters._

object OrExpressionVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_OrExpression(ctx: CypherParser.OC_OrExpressionContext): Expression =
    ctx.oC_XorExpression().asScala.toList.foldRight(Expression.mkLiteral(Value.False))((innerCtx, exp) => Expression.BinOp(Operator.Or,exp,innerCtx.accept(XorVisitor)))
}
