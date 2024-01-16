package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.utils.Helpers.maybeMatch
import com.quine.language.ast.{Expression, Operator}

import scala.collection.JavaConverters._

object NonArithmeticOperatorVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_NonArithmeticOperatorExpression(ctx: CypherParser.OC_NonArithmeticOperatorExpressionContext): Expression = {
    val init = ctx.oC_Atom().accept(AtomVisitor)

    ctx.oC_PropertyLookup().asScala.toList.foldLeft(init){ (exp, innerCtx) =>
      Expression.BinOp(Operator.Dot, exp, Expression.Ident(Symbol(innerCtx.getText.substring(1))))
    }
  }
}
