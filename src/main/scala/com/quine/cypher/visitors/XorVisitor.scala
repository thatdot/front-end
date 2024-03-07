package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.expressions.AndVisitor
import com.quine.language.ast.{Expression, Operator, Source, Value}

import collection.JavaConverters._

object XorVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_XorExpression(ctx: CypherParser.OC_XorExpressionContext): Expression = {
    val children = ctx.oC_AndExpression().asScala.toList
    if(children.size == 1) {
      children.head.accept(AndVisitor)
    } else {
      val src = Source.TextSource(
        start = ctx.start.getStartIndex,
        end = ctx.stop.getStopIndex,
        text = ctx.getText
      )
      children.foldRight(Expression.mkLiteral(Source.NoSource, Value.False))((innerCtx, exp) => Expression.BinOp(src, Operator.XOr, exp, innerCtx.accept(AndVisitor)))
    }
  }
}
