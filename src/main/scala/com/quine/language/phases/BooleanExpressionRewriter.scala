package com.quine.cypher.phases
import com.quine.language.ast.{Expression, Operator, Predicate, Value}

object BooleanExpressionRewriter extends Rewriter {
  def rewrite(exp: Expression): Expression = exp match {
    case lit: Expression.Literal => lit
    case id: Expression.Ident => id
    case p: Expression.Parameter => p
    case Expression.Apply(name, args) => Expression.Apply(name, args.map(rewrite))
    case Expression.UnaryOp(op, exp) => op match {
      case Operator.Plus => ???
      case Operator.Minus => ???
      case Operator.Dot => ???
      case Operator.Equals => ???
      case Operator.LessThan => ???
      case Operator.And => ???
      case Operator.Or => ???
      case Operator.XOr => ???
      case Operator.Not => rewrite(exp) match {
        case Expression.Literal(Value.True) => Expression.Literal(Value.False)
        case Expression.Literal(Value.False) => Expression.Literal(Value.True)
        case other => Expression.UnaryOp(Operator.Not, other)
      }
    }
    case Expression.BinOp(op, lhs, rhs) =>
      val exps = (rewrite(lhs), rewrite(rhs))
      op match {
        case Operator.Plus => exps match {
          case (Expression.Literal(Value.Integral(0)), rhs) => rhs
          case (lhs, Expression.Literal(Value.Integral(0))) => lhs
          case (_, _) => Expression.BinOp(Operator.Plus, exps._1, exps._2)
        }
        case Operator.Minus => exps match {
          case (lhs, Expression.Literal(Value.Integral(0))) => lhs
          case (_, _) => Expression.BinOp(Operator.Minus, exps._1, exps._2)
        }
        case Operator.Dot => Expression.BinOp(Operator.Dot, exps._1, exps._2)
        case Operator.Equals => Expression.BinOp(Operator.Equals, exps._1, exps._2)
        case Operator.LessThan => ???
        case Operator.And => exps match {
          case (Expression.Literal(Value.True), _) => exps._2
          case (_, Expression.Literal(Value.True)) => exps._1
          case (_, _) => Expression.BinOp(Operator.And, exps._1, exps._2)
        }
        case Operator.Or => exps match {
          case (Expression.Literal(Value.False), _) => exps._2
          case (_, Expression.Literal(Value.False)) => exps._1
          case (_, _) => Expression.BinOp(Operator.Or, exps._1, exps._2)
        }
        case Operator.XOr => exps match {
          case (Expression.Literal(Value.False), _) => exps._2
          case (_, Expression.Literal(Value.False)) => exps._1
          case (_ , _) => Expression.BinOp(Operator.XOr, exps._1, exps._2)
        }
        case Operator.Not => ???
      }
  }

  override def rewrite(p: Predicate): Predicate = p match {
    case en: Predicate.ExistsNode => en
    case ee: Predicate.ExistsEdge => ee
    case Predicate.And(lhs, rhs) => Predicate.And(rewrite(lhs), rewrite(rhs))
    case Predicate.Or(lhs, rhs) => Predicate.Or(rewrite(lhs), rewrite(rhs))
    case Predicate.True => Predicate.True
    case Predicate.False => Predicate.False
    case Predicate.Satisfies(pexp) => Predicate.Satisfies(rewrite(pexp))
  }
}
