package com.quine.cypher

import com.quine.cypher.builders.ParserBuilder
import com.quine.cypher.parsing.{CypherLexer, CypherParser}
import com.quine.cypher.phases.{BooleanExpressionRewriter, PredicateLogicRewriter}
import com.quine.cypher.visitors.QueryVisitor
import org.antlr.v4.runtime._
import com.quine.language.ast.Expression._
import com.quine.language.ast.Operator._
import com.quine.language.ast.Value._

object Program {

  val tq0 =
    """MATCH (s:Source)-[:edge]->(d:Dest) RETURN s.x + d.x""".stripMargin

  val tq1 =
    """MATCH (l) WHERE id(l) = $that.data.id
      |MATCH (v) WHERE id(v) = idFrom('verb', l.verb)
      |SET v.type = 'verb',
      |    v.verb = l.verb
      |CREATE (l)-[:verb]->(v)
      |""".stripMargin

  val tq2 =
    """
      |MATCH (e1)-[:EVENT]->(f)<-[:EVENT]-(e2),
      |              (f)<-[:EVENT]-(e3)<-[:EVENT]-(p2)-[:EVENT]->(e4)
      |        WHERE e1.type = "WRITE"
      |          AND e2.type = "READ"
      |          AND e3.type = "DELETE"
      |          AND e4.type = "SEND"
      |        RETURN DISTINCT id(f) as fileId
      |""".stripMargin

  val tq3 =
    """
      |MATCH (n:arctan) WHERE n.approximation IS NOT NULL AND n.denominator IS NOT NULL RETURN DISTINCT id(n) AS id
      |""".stripMargin

  val tq4 =
    """
      |MATCH (l) WHERE l.type = 'log' RETURN DISTINCT id(l) AS id
      |""".stripMargin

  val tq5 =
    """
      |MATCH (server1:server)<-[:TARGETED]-(event1 {cache_class:"MISS"})-[:REQUESTED]->(asset)<-[:REQUESTED]-(event2 {cache_class:"MISS"})-[:TARGETED]->(server2:server)
      |        RETURN DISTINCT id(event1) AS event1
      |""".stripMargin

  val tq6 =
    """
      |MATCH (n) RETURN DISTINCT id(n) AS id
      |""".stripMargin

  val tq7 =
    """
      |MATCH
      |  (tainted:account)<-[:from]-(tx:transaction)-[:to]->(otherAccount:account),
      |  (tx)-[:defined_in]->(ba:block_assoc)
      |WHERE
      |  tainted.tainted IS NOT NULL
      |RETURN
      |  id(tainted) AS accountId,
      |  tainted.tainted AS oldTaintedLevel,
      |  id(otherAccount) AS otherAccountId
      |""".stripMargin

  val tq8 =
    """
      |MATCH (investment:investment)<-[:HOLDS]-(desk:desk)<-[:HAS]-(institution:institution)
      |RETURN DISTINCT id(investment) AS id
      |""".stripMargin

  def main(args: Array[String]): Unit = {
    val parser = ParserBuilder.mkParser(List(PredicateLogicRewriter, BooleanExpressionRewriter))

    parser.parseCypher(tq8) match {
      case Left(value) =>
        println("***************************** Error ******************************")
        println(value)
      case Right(value) =>
        println("***************************** Success ******************************")
        println(value)
    }
  }
}