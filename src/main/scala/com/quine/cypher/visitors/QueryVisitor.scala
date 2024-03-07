package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_QueryContext
import com.quine.language.ast.Query

object QueryVisitor extends CypherBaseVisitor[Option[Query]] {
    override def visitOC_Query(ctx: OC_QueryContext): Option[Query] = {
        ctx.oC_RegularQuery().accept(RegularQueryVisitor)
    }
}