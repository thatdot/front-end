package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.{Expression, Source}

object VariableVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_Variable(ctx: CypherParser.OC_VariableContext): Expression = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )
    Expression.Ident(src, Symbol(ctx.oC_SymbolicName().getText))
  }
}
