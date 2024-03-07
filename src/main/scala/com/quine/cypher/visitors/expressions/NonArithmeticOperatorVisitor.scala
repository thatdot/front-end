package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.utils.Helpers.maybeMatch
import com.quine.language.ast.{Expression, Operator, Source}

import scala.collection.JavaConverters._

object NonArithmeticOperatorVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_NonArithmeticOperatorExpression(ctx: CypherParser.OC_NonArithmeticOperatorExpressionContext): Expression = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )

    val init = ctx.oC_Atom().accept(AtomVisitor)

    ctx.oC_PropertyLookup().asScala.toList.foldLeft(init){ (exp, innerCtx) =>
      val innerSrc = Source.TextSource(
        start = innerCtx.start.getStartIndex,
        end = innerCtx.stop.getStopIndex,
        text = ctx.getText
      )

      Expression.BinOp(src, Operator.Dot, exp, Expression.Ident(innerSrc, Symbol(innerCtx.getText.substring(1))))
    }
  }
}
