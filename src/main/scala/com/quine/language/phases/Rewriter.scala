package com.quine.cypher.phases

import com.quine.language.ast.Predicate

trait Rewriter {
  def rewrite(ast: Predicate): Predicate
}
