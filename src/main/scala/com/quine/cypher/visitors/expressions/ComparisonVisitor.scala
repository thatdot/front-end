package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.StringListNullVisitor
import com.quine.language.ast.{Expression, Operator, Source}

import scala.collection.JavaConverters._

object ComparisonVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_ComparisonExpression(ctx: CypherParser.OC_ComparisonExpressionContext): Expression = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )

    val lhs = ctx.oC_StringListNullPredicateExpression().accept(StringListNullVisitor)
    val rhs = ctx.oC_PartialComparisonExpression().asScala.toList.map(inner => inner.oC_StringListNullPredicateExpression().accept(StringListNullVisitor))
    if(rhs.isEmpty) {
      lhs
    } else {
      Expression.BinOp(src, Operator.Equals, lhs, rhs.head)
    }
  }
}
