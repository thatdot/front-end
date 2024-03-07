package com.quine.cypher.phases
import com.quine.language.ast.{Expression, Operator, Predicate, Value}

object BooleanExpressionRewriter extends Rewriter {
  def rewrite(exp: Expression): Expression = exp match {
    case lit: Expression.Literal => lit
    case id: Expression.Ident => id
    case p: Expression.Parameter => p
    case Expression.Apply(src, name, args) => Expression.Apply(src, name, args.map(rewrite))
    case Expression.UnaryOp(src, op, exp) => op match {
      case Operator.Plus => ???
      case Operator.Minus => ???
      case Operator.Dot => ???
      case Operator.Equals => ???
      case Operator.LessThan => ???
      case Operator.And => ???
      case Operator.Or => ???
      case Operator.XOr => ???
      case Operator.Not => rewrite(exp) match {
        case Expression.Literal(src, Value.True) => Expression.Literal(src, Value.False)
        case Expression.Literal(src, Value.False) => Expression.Literal(src, Value.True)
        case other => Expression.UnaryOp(src, Operator.Not, other)
      }
    }
    case Expression.BinOp(src, op, lhs, rhs) =>
      val exps = (rewrite(lhs), rewrite(rhs))
      op match {
        case Operator.Plus => exps match {
          case (Expression.Literal(src, Value.Integral(0)), rhs) => rhs
          case (lhs, Expression.Literal(src, Value.Integral(0))) => lhs
          case (_, _) => Expression.BinOp(src, Operator.Plus, exps._1, exps._2)
        }
        case Operator.Minus => exps match {
          case (lhs, Expression.Literal(src, Value.Integral(0))) => lhs
          case (_, _) => Expression.BinOp(src, Operator.Minus, exps._1, exps._2)
        }
        case Operator.Dot => Expression.BinOp(src, Operator.Dot, exps._1, exps._2)
        case Operator.Equals => Expression.BinOp(src, Operator.Equals, exps._1, exps._2)
        case Operator.LessThan => ???
        case Operator.And => exps match {
          case (Expression.Literal(src, Value.True), _) => exps._2
          case (_, Expression.Literal(src, Value.True)) => exps._1
          case (_, _) => Expression.BinOp(src, Operator.And, exps._1, exps._2)
        }
        case Operator.Or => exps match {
          case (Expression.Literal(src, Value.False), _) => exps._2
          case (_, Expression.Literal(src, Value.False)) => exps._1
          case (_, _) => Expression.BinOp(src, Operator.Or, exps._1, exps._2)
        }
        case Operator.XOr => exps match {
          case (Expression.Literal(src, Value.False), _) => exps._2
          case (_, Expression.Literal(src, Value.False)) => exps._1
          case (_ , _) => Expression.BinOp(src, Operator.XOr, exps._1, exps._2)
        }
        case Operator.Not => ???
      }
  }

  override def rewrite(p: Predicate): Predicate = p match {
    case en: Predicate.ExistsNode => en
    case ee: Predicate.ExistsEdge => ee
    case Predicate.And(src, lhs, rhs) => Predicate.And(src, rewrite(lhs), rewrite(rhs))
    case Predicate.Or(src, lhs, rhs) => Predicate.Or(src, rewrite(lhs), rewrite(rhs))
    case Predicate.True => Predicate.True
    case Predicate.False => Predicate.False
    case Predicate.Satisfies(src, pexp) => Predicate.Satisfies(src, rewrite(pexp))
  }
}
