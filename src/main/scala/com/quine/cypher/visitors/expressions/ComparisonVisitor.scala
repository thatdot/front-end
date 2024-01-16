package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.StringListNullVisitor
import com.quine.language.ast.{Expression, Operator}

import scala.collection.JavaConverters._

object ComparisonVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_ComparisonExpression(ctx: CypherParser.OC_ComparisonExpressionContext): Expression = {
    val lhs = ctx.oC_StringListNullPredicateExpression().accept(StringListNullVisitor)
    val rhs = ctx.oC_PartialComparisonExpression().asScala.toList.map(inner => inner.oC_StringListNullPredicateExpression().accept(StringListNullVisitor))
    if(rhs.isEmpty) {
      lhs
    } else {
      Expression.BinOp(Operator.Equals, lhs, rhs.head)
    }
  }
}
