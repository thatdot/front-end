package com.quine.cypher.phases

import com.quine.language.ast.Predicate

object PredicateLogicRewriter extends Rewriter {
  override def rewrite(ast: Predicate): Predicate = ast match {
    case en: Predicate.ExistsNode => en
    case ee: Predicate.ExistsEdge => ee
    case Predicate.And(lhs, rhs) => (rewrite(lhs), rewrite(rhs)) match {
      case (Predicate.True, newRhs) => newRhs
      case (newLhs, Predicate.True) => newLhs
      case (newLhs, newRhs) => Predicate.And(newLhs, newRhs)
    }
    case Predicate.Or(lhs, rhs) => (rewrite(lhs), rewrite(rhs)) match {
      case (Predicate.False, newRhs) => newRhs
      case (newLhs, Predicate.False) => newLhs
      case (newLhs, newRhs) => Predicate.Or(newLhs, newRhs)
    }
    case Predicate.True => Predicate.True
    case Predicate.False => Predicate.False
    case s: Predicate.Satisfies => s
  }
}
