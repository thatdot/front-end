package com.quine.cypher.visitors

import com.quine.cypher.parsing.CypherParser.OC_PowerOfExpressionContext
import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.expressions.PowerOfVisitor
import com.quine.language.ast.{Expression, Operator, Source}
import org.antlr.v4.runtime.tree.{RuleNode, TerminalNode}

import collection.JavaConverters._
import scala.collection.immutable.Queue

object MultiplyDivideModuloVisitor extends CypherBaseVisitor[Expression] {
  override def visitOC_MultiplyDivideModuloExpression(ctx: CypherParser.OC_MultiplyDivideModuloExpressionContext): Expression = {
    val children = ctx.children.asScala.toList
    if(children.size == 1) {
      children.head.accept(PowerOfVisitor)
    } else {
      val src = Source.TextSource(
        start = ctx.start.getStartIndex,
        end = ctx.stop.getStopIndex,
        text = ctx.getText
      )

      val (ops, exps) = ctx.children.asScala.toList.foldLeft(List.empty[Operator] -> Queue.empty[Expression]) { (mem, pt) =>
        pt match {
          case po: OC_PowerOfExpressionContext => mem._1 -> mem._2.enqueue(po.accept(PowerOfVisitor))
          case node: TerminalNode => node.getText.trim match {
            case "" => mem
          }
        }
      }

      val (init, rexps) = exps.dequeue

      ops.foldLeft(init -> rexps) {
        case ((e1, rem), op) =>
          val (e2, r2) = rem.dequeue
          Expression.BinOp(src, op, e1, e2) -> r2
      }._1
    }
  }
}
