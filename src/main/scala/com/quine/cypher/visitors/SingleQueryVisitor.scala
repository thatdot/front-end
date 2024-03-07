package com.quine.cypher.visitors

import cats.implicits._
import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_SingleQueryContext
import com.quine.language.ast.Query

object SingleQueryVisitor extends CypherBaseVisitor[Option[Query]] {
  override def visitOC_SingleQuery(ctx: OC_SingleQueryContext): Option[Query] = {
    val r1 = if(ctx.oC_SinglePartQuery() == null) {
        Option.empty[Query]
    } else {
        Some(ctx.oC_SinglePartQuery().accept(SinglePartQueryVisitor))
    }
    val r2 = if(ctx.oC_MultiPartQuery() == null) {
        Option.empty[Query]
    } else {
        Some(ctx.oC_MultiPartQuery().accept(MultiPartQueryVisitor))
    }

    (r1 <+> r2)
  }
}
