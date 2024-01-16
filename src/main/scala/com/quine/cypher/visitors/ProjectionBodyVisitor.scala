package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.Projection

import collection.JavaConverters._

object ProjectionBodyVisitor extends CypherBaseVisitor[Projection] {
  override def visitOC_ProjectionBody(ctx: CypherParser.OC_ProjectionBodyContext): Projection = {
    Projection.Parallel(ctx.oC_ProjectionItems().oC_ProjectionItem().asScala.toList.map(innerCtx => innerCtx.accept(ProjectionItemVisitor)))
  }
}
