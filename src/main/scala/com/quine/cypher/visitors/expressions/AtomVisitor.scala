package com.quine.cypher.visitors.expressions

import cats.implicits._
import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.utils.Helpers.maybeMatch
import com.quine.cypher.visitors.{FunctionInvocationVisitor, VariableVisitor}
import com.quine.language.ast.Expression

object AtomVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_Atom(ctx: CypherParser.OC_AtomContext): Expression = {
    val maybeLiteral = maybeMatch(ctx.oC_Literal(), LiteralVisitor)
    val maybeApply = maybeMatch(ctx.oC_FunctionInvocation(), FunctionInvocationVisitor)
    val maybeVariable = maybeMatch(ctx.oC_Variable(), VariableVisitor)
    val maybeParameter = maybeMatch(ctx.oC_Parameter(), ParameterVisitor)

    (maybeApply <+> maybeLiteral <+> maybeVariable <+> maybeParameter).get
  }
}
