package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_UnwindContext
import com.quine.language.ast.Predicate

object UnwindClauseVisitor extends CypherBaseVisitor[Predicate] {
  override def visitOC_Unwind(ctx: OC_UnwindContext): Predicate = {
    ???
    //ReadClause.UnwindClause(ctx.getText())
  }  
}
