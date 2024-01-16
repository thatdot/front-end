package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_VariableContext
import com.quine.language.ast.Predicate

object VariablePatternVisitor extends CypherBaseVisitor[Predicate] {
    
  override def visitOC_Variable(ctx: OC_VariableContext): Predicate = {
    ???
    //VariablePatternExp(ctx.getText())
  }
}
