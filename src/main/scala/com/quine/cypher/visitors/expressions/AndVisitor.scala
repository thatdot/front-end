package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.{Expression, Operator, Source, Value}

import scala.collection.JavaConverters._

object AndVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_AndExpression(ctx: CypherParser.OC_AndExpressionContext): Expression = {
    val children = ctx.oC_NotExpression().asScala.toList
    if(children.size == 1) {
      children.head.oC_ComparisonExpression().accept(ComparisonVisitor)
    } else {
      val src = Source.TextSource(
        start = ctx.start.getStartIndex,
        end = ctx.stop.getStopIndex,
        text = ctx.getText
      )
      children.foldRight(Expression.mkLiteral(Source.NoSource, Value.True))((innerCtx, exp) => Expression.BinOp(src, Operator.And, exp, innerCtx.oC_ComparisonExpression().accept(ComparisonVisitor)))
    }
  }
}
