package com.quine.sql.utils

object CheatingNameGenerator {
  var counter: Int = 0

  def synthesizeIdent(): Symbol = {
    counter = counter + 1
    Symbol(s"anonymous_$counter")
  }
}
