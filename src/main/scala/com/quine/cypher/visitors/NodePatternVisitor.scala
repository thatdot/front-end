package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_NodePatternContext
import com.quine.cypher.utils.CheatingNameGenerator
import com.quine.language.ast.{Predicate, Source}

import collection.JavaConverters._

object NodePatternVisitor extends CypherBaseVisitor[Predicate] {
  override def visitOC_NodePattern(ctx: OC_NodePatternContext): Predicate = {
    val src = Source.TextSource(
      start = ctx.start.getStartIndex,
      end = ctx.stop.getStopIndex,
      text = ctx.getText
    )

    val labels = if(ctx.oC_NodeLabels() == null) List.empty[Symbol] else ctx.oC_NodeLabels().oC_NodeLabel().asScala.toList.map(innerCtx => innerCtx.accept(NodeLabelVisitor))
    val properties = ctx.oC_Properties()
    val binding = if(ctx.oC_Variable() == null) CheatingNameGenerator.synthesizeIdent() else Symbol(ctx.oC_Variable().getText)
    Predicate.ExistsNode(src, binding, labels)
  }
}
