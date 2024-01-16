package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.Expression

object ParameterVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_Parameter(ctx: CypherParser.OC_ParameterContext): Expression =
    Expression.Parameter(Symbol(ctx.getText))
}
