package com.quine.cypher.visitors

import cats.implicits._
import com.quine.cypher.parsing.CypherBaseVisitor
import com.quine.cypher.parsing.CypherParser.OC_ReadingClauseContext
import com.quine.language.ast.Predicate

object ReadingClauseVisitor extends CypherBaseVisitor[Predicate] {
  override def visitOC_ReadingClause(ctx: OC_ReadingClauseContext): Predicate = {
    val r1 = if(ctx.oC_Match() == null) {
        Option.empty[Predicate]
    } else {
        Some(ctx.oC_Match().accept(MatchClauseVisitor))
    }

    val r2 =  if(ctx.oC_Unwind() == null) {
        Option.empty[Predicate]
    } else {
        Some(ctx.oC_Unwind().accept(UnwindClauseVisitor))
    }

    (r1 <+> r2).get
  }
}
