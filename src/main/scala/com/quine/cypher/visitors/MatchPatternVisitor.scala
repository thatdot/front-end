package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_PatternContext
import com.quine.language.ast.{Predicate, Source}

import collection.JavaConverters._

object MatchPatternVisitor extends CypherBaseVisitor[Predicate] {
    override def visitOC_Pattern(ctx: OC_PatternContext): Predicate = {
        val parts = ctx.oC_PatternPart().asScala.toList

        val src = Source.TextSource(
            start = ctx.start.getStartIndex,
            end = ctx.stop.getStopIndex,
            text = ctx.getText
        )

        if(parts.size == 1) {
            ctx.accept(PatternExpVisitor)
        } else {
            parts.foldRight(Predicate.sat)((innerCtx, pred) => Predicate.And(src, pred, innerCtx.accept(PatternExpVisitor)))
        }
    }
}
