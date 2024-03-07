package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.XorVisitor
import com.quine.language.ast.{Expression, Operator, Source, Value}

import scala.collection.JavaConverters._

object OrExpressionVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_OrExpression(ctx: CypherParser.OC_OrExpressionContext): Expression = {
    val children = ctx.oC_XorExpression().asScala.toList
    if(children.size == 1) {
      children.head.accept(XorVisitor)
    } else {
      val src = Source.TextSource(
        start = ctx.start.getStartIndex,
        end = ctx.stop.getStopIndex,
        text = ctx.getText
      )
      children.foldRight(Expression.mkLiteral(Source.NoSource, Value.False))((innerCtx, exp) => Expression.BinOp(src, Operator.Or,exp,innerCtx.accept(XorVisitor)))
    }

  }
}
