package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_RegularQueryContext
import com.quine.language.ast.{Query, Source}

import collection.JavaConverters._

object RegularQueryVisitor extends CypherBaseVisitor[Option[Query]] {
  override def visitOC_RegularQuery(ctx: OC_RegularQueryContext): Option[Query] = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )

    val lhs = ctx.oC_SingleQuery().accept(SingleQueryVisitor)
    val rhs = ctx.oC_Union().asScala.foldRight(Option.apply(Query.empty)) { (innerCtx, union) =>
      for {
        iq <- innerCtx.oC_SingleQuery().accept(SingleQueryVisitor)
        wq <- union
      } yield {
        Query.Union(Source.NoSource, iq, wq)
      }
    }

    for {
      l <- lhs
      r <- rhs
    } yield {
      Query.Union(src, l, r)
    }
  }
}
