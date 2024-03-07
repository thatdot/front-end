package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.{Expression, Source, Value}

object IntegerVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_IntegerLiteral(ctx: CypherParser.OC_IntegerLiteralContext): Expression = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )

    Expression.Literal(src, Value.Integral(ctx.DecimalInteger().getText.toInt))
  }
}
