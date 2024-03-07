package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.{Expression, Source}

object ParameterVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_Parameter(ctx: CypherParser.OC_ParameterContext): Expression = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )

    Expression.Parameter(src, Symbol(ctx.getText))
  }
}
