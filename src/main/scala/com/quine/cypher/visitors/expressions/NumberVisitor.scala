package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.utils.Helpers.maybeMatch
import com.quine.language.ast.Expression

object NumberVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_NumberLiteral(ctx: CypherParser.OC_NumberLiteralContext): Expression = {
    val maybeInt = maybeMatch(ctx.oC_IntegerLiteral(), IntegerVisitor)

    maybeInt.get
  }
}
