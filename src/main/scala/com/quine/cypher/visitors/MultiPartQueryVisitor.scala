package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_MultiPartQueryContext
import com.quine.language.ast.Query

object MultiPartQueryVisitor extends CypherBaseVisitor[Query] {
  override def visitOC_MultiPartQuery(ctx: OC_MultiPartQueryContext): Query = {
    ???
  }
}
