package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_RegularQueryContext
import com.quine.language.ast.Query

import collection.JavaConverters._

object RegularQueryVisitor extends CypherBaseVisitor[Query] {
  override def visitOC_RegularQuery(ctx: OC_RegularQueryContext): Query = {
    val lhs = ctx.oC_SingleQuery().accept(SingleQueryVisitor)
    val rhs = ctx.oC_Union().asScala.foldRight(Query.empty) { (innerCtx, union) =>
        Query.Union(innerCtx.oC_SingleQuery().accept(SingleQueryVisitor), union)
    }

    Query.Union(lhs, rhs)
  }
}
