package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.{Expression, Operator}

import scala.collection.JavaConverters._

object PropertyVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_PropertyExpression(ctx: CypherParser.OC_PropertyExpressionContext): Expression = {
    val of = ctx.oC_Atom().accept(AtomVisitor)
    if(ctx.oC_PropertyLookup().size() > 1) {
      throw new RuntimeException("This makes no sense.")
    }
    Expression.BinOp(Operator.Dot, of, Expression.Ident(Symbol(ctx.oC_PropertyLookup(0).getText.substring(1))))
  }
}
