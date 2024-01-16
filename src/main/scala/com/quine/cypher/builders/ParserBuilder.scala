package com.quine.cypher.builders

import com.quine.cypher.Parser
import com.quine.cypher.phases.Rewriter

object ParserBuilder {
  def mkParser(rewriters: List[Rewriter]): Parser =
    new Parser(rewriters)
}
