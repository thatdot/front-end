package com.quine.language.ast

trait Traversal {
  def traverseQuery(q: Query): Query = q match {
    case Query.Union(src, lhs, rhs) => ???
    case Query.Single(src, predicate, effects, maybeProjection) => ???
    case Query.Empty => ???
  }
}

trait Transform[A] {
  def transform(a: A): A
}