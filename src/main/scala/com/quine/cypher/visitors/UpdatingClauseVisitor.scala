package com.quine.cypher.visitors

import cats.implicits._
import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.language.ast.Effect

import collection.JavaConverters._

object UpdatingClauseVisitor extends CypherBaseVisitor[List[Effect]] {
  override def visitOC_UpdatingClause(ctx: CypherParser.OC_UpdatingClauseContext): List[Effect] = {
    val maybeSets = if(ctx.oC_Set() == null) {
      Option.empty[List[Effect]]
    } else {
      Some(ctx.oC_Set().oC_SetItem().asScala.toList.map(_.accept(SetItemVisitor)))
    }

    val maybeCreate = if(ctx.oC_Create() == null) {
      Option.empty[List[Effect]]
    } else {
      Some(List(Effect.Create(ctx.oC_Create().getText)))
    }

    (maybeSets <+> maybeCreate).get
  }
}
