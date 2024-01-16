package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.{Expression, Value}

object IntegerVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_IntegerLiteral(ctx: CypherParser.OC_IntegerLiteralContext): Expression =
    Expression.Literal(Value.Integral(ctx.DecimalInteger().getText.toInt))
}
