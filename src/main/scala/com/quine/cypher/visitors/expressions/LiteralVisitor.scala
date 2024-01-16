package com.quine.cypher.visitors.expressions

import cats.implicits._
import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.utils.Helpers.maybeMatch
import com.quine.language.ast.{Expression, Value}

object LiteralVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_Literal(ctx: CypherParser.OC_LiteralContext): Expression = {
    val maybeNumber = maybeMatch(ctx.oC_NumberLiteral(), NumberVisitor)
    val maybeString = if(ctx.StringLiteral() == null) {
      Option.empty[Expression.Literal]
    } else {
      Some(Expression.Literal(Value.Text(ctx.StringLiteral().getText)))
    }

    (maybeNumber <+> maybeString).get
  }
}
