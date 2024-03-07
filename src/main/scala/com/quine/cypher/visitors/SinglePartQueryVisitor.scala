package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_SinglePartQueryContext
import com.quine.cypher.utils.Helpers.maybeMatch
import com.quine.language.ast.{Predicate, Query, Source}

import collection.JavaConverters._

object SinglePartQueryVisitor extends CypherBaseVisitor[Query] {
  override def visitOC_SinglePartQuery(ctx: OC_SinglePartQueryContext): Query = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )
    Query.Single(
      source = src,
      predicate = ctx.oC_ReadingClause().asScala.toList.foldRight(Predicate.sat)((innerCtx, pred) => Predicate.And(Source.NoSource, pred, innerCtx.accept(ReadingClauseVisitor))),
      effects = ctx.oC_UpdatingClause().asScala.toList.flatMap(_.accept(UpdatingClauseVisitor)),
      maybeProjection = maybeMatch(ctx.oC_Return(), ReturnVisitor)
    )
  }
}
