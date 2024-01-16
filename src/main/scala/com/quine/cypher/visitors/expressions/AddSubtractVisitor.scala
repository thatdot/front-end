package com.quine.cypher.visitors.expressions

import com.quine.cypher.parsing.CypherParser.OC_MultiplyDivideModuloExpressionContext
import com.quine.cypher.parsing.{CypherBaseVisitor, CypherParser}
import com.quine.cypher.visitors.MultiplyDivideModuloVisitor
import com.quine.language.ast.{Expression, Operator}
import org.antlr.v4.runtime.tree.TerminalNode

import scala.collection.JavaConverters._
import scala.collection.immutable.Queue

object AddSubtractVisitor extends CypherBaseVisitor[Expression] {

  override def visitOC_AddOrSubtractExpression(ctx: CypherParser.OC_AddOrSubtractExpressionContext): Expression = {
    val (ops, exps) = ctx.children.asScala.toList.foldLeft(List.empty[Operator] -> Queue.empty[Expression]){ (mem, pt) =>
      pt match {
        case mdmec: OC_MultiplyDivideModuloExpressionContext => mem._1 -> mem._2.enqueue(mdmec.accept(MultiplyDivideModuloVisitor))
        case node: TerminalNode => node.getText.trim match {
          case "" => mem
          case "+" => (Operator.Plus::mem._1) -> mem._2
          case "-" => (Operator.Minus::mem._1) -> mem._2
        }
      }
    }

    val (init, rexps) = exps.dequeue

    val (exp, _) = ops.foldLeft(init -> rexps) {
      case ((e1, rem), op) =>
        val (e2, r2) = rem.dequeue
        Expression.BinOp(op, e1, e2) -> r2
    }

    exp
  }
}
