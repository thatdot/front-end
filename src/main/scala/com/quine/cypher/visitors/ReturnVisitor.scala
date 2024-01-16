package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.Projection

object ReturnVisitor extends CypherBaseVisitor[Projection] {
  override def visitOC_Return(ctx: CypherParser.OC_ReturnContext): Projection = {
    ctx.oC_ProjectionBody().accept(ProjectionBodyVisitor)
  }
}
