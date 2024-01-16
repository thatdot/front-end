package com.quine.language.ast

trait Traversal {
  def traverseQuery(q: Query): Query = q match {
    case Query.Union(lhs, rhs) => ???
    case Query.Single(predicate, effects, maybeProjection) => ???
    case Query.Empty => ???
  }
}

trait Transform[A] {
  def transform(a: A): A
}