package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast
import com.quine.language.ast.{Expression, Operator, Predicate}

object NodeLabelVisitor extends CypherBaseVisitor[Symbol] {
  override def visitOC_NodeLabel(ctx: CypherParser.OC_NodeLabelContext): Symbol = {
    Symbol(ctx.getText())
  }
}
