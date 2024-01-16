package com.quine.cypher.visitors

import cats.implicits._
import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_PatternPartContext
import com.quine.cypher.visitors.expressions.VariablePatternVisitor
import com.quine.language.ast.Predicate

object PatternExpVisitor extends CypherBaseVisitor[Predicate] {

  override def visitOC_PatternPart(ctx: OC_PatternPartContext): Predicate = {

    val r1 = if(ctx.oC_AnonymousPatternPart() == null) {
        Option.empty[Predicate]
    } else {
        Some(ctx.oC_AnonymousPatternPart().accept(AnonymousPatternVisitor))
    }

    val r2 = if(ctx.oC_Variable() == null) {
        Option.empty[Predicate]
    } else {
        Some(ctx.oC_Variable().accept(VariablePatternVisitor))
    }

    (r1 <+> r2).get
  }
}
