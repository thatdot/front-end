package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_PatternContext
import com.quine.language.ast.Predicate

import collection.JavaConverters._

object MatchPatternVisitor extends CypherBaseVisitor[Predicate] {
    override def visitOC_Pattern(ctx: OC_PatternContext): Predicate = {
        ctx.oC_PatternPart().asScala.toList.foldRight(Predicate.sat)((innerCtx, pred) => Predicate.And(pred, innerCtx.accept(PatternExpVisitor)))
    }
}
