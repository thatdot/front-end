package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.expressions.ExpressionVisitor
import com.quine.language.ast.Projection

object ProjectionItemVisitor extends CypherBaseVisitor[Projection] {
  override def visitOC_ProjectionItem(ctx: CypherParser.OC_ProjectionItemContext): Projection = {
    val expression = ctx.oC_Expression().accept(ExpressionVisitor)
    val alias = if(ctx.oC_Variable() == null) {
      ctx.oC_Expression().getText
    } else {
      ctx.oC_Variable().getText
    }

    Projection.Calculation(expression, Symbol(alias))
  }

}
