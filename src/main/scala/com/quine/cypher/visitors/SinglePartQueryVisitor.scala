package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_SinglePartQueryContext
import com.quine.cypher.utils.Helpers.maybeMatch
import com.quine.language.ast.{Predicate, Query}

import collection.JavaConverters._

object SinglePartQueryVisitor extends CypherBaseVisitor[Query] {
  override def visitOC_SinglePartQuery(ctx: OC_SinglePartQueryContext): Query = {
    Query.Single(
      predicate = ctx.oC_ReadingClause().asScala.toList.foldRight(Predicate.sat)((innerCtx, pred) => Predicate.And(pred, innerCtx.accept(ReadingClauseVisitor))),
      effects = ctx.oC_UpdatingClause().asScala.toList.flatMap(_.accept(UpdatingClauseVisitor)),
      maybeProjection = maybeMatch(ctx.oC_Return(), ReturnVisitor)
    )
  }
}
