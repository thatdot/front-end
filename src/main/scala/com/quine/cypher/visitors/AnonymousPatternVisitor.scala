package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_AnonymousPatternPartContext
import com.quine.language.ast.Predicate

object AnonymousPatternVisitor extends CypherBaseVisitor[Predicate] {
  override def visitOC_AnonymousPatternPart(ctx: OC_AnonymousPatternPartContext): Predicate = {
    ctx.oC_PatternElement().accept(PatternElementVisitor)
  }
}
