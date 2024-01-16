package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.Expression

object VariableVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_Variable(ctx: CypherParser.OC_VariableContext): Expression =
    Expression.Ident(Symbol(ctx.oC_SymbolicName().getText))
}
