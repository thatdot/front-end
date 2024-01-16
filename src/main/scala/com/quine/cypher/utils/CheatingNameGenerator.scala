package com.quine.cypher.utils

object CheatingNameGenerator {
  var counter: Int = 0

  def synthesizeIdent(): Symbol = {
    counter = counter + 1
    Symbol(s"anonymous_$counter")
  }
}
