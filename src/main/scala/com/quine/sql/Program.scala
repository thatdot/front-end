package com.quine.sql

object Program {

  val tq1 =
    """
      |SELECT
      |  s.x + d.x
      |FROM
      |  Source s
      |  INNER JOIN Dest d THROUGH OUTGOING EDGE Edge
      |""".stripMargin

  def main(args: Array[String]): Unit = {
    println(SQLParser.selectBit.parse("SELECT s.x + d.x"))
    val result = SQLParser.query.parseAll(tq1)
    println(result)
  }
}
