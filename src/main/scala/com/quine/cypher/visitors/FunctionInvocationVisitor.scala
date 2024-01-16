package com.quine.cypher.visitors

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.expressions.ExpressionVisitor
import com.quine.language.ast.Expression

import collection.JavaConverters._

object FunctionInvocationVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_FunctionInvocation(ctx: CypherParser.OC_FunctionInvocationContext): Expression = {
    val fname = ctx.oC_FunctionName().getText
    val fargs = ctx.oC_Expression().asScala.toList.map(innerCtx => innerCtx.accept(ExpressionVisitor))

    Expression.Apply(Symbol(fname), fargs)
  }
}
