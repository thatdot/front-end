package com.quine.sql

import cats.data.NonEmptyList
import cats.parse.Rfc5234.{alpha, digit, wsp}
import cats.parse.{Parser, Parser0}
import com.quine.cypher.phases.{BooleanExpressionRewriter, Rewriter}
import com.quine.language.ast.{Effect, Expression, Operator, Predicate, Projection, Query, Value}
import com.quine.sql.utils.CheatingNameGenerator

object SQLParser {
  val whitespace: Parser[Unit] = Parser.charIn(" \t\r\n").void
  val whitespaces1: Parser[Unit] = whitespace.rep.void
  val whitespaces0: Parser0[Unit] = whitespace.rep0.void

  implicit class ParserExts[A](p: Parser[A]) {
    def $[B](p1: Parser[B]): Parser[(A, B)] = p ~ (whitespaces1 *> p1)

    def #>[B](p1: Parser[B]): Parser[B] = p *> whitespaces1 *> p1
  }

  val listSep: Parser[Unit] =
    Parser.char(',').soft.surroundedBy(whitespaces0).void

  def rep[A](pa: Parser[A]): Parser0[List[A]] =
    pa.repSep0(listSep).surroundedBy(whitespaces0)

  def rep1[A](pa: Parser[A]): Parser[NonEmptyList[A]] =
    pa.repSep(listSep).surroundedBy(whitespaces0)

  sealed trait JoinType

  object JoinType {
    case object Inner extends JoinType
    case object Outer extends JoinType
  }

  sealed trait Direction

  object Direction {
    case object Outgoing extends Direction
  }

  val select = Parser.string("SELECT").void
  val from = Parser.string("FROM").void
  val where = Parser.string("WHERE").void
  val as = Parser.string("AS").void
  val left = Parser.string("LEFT")
  val right = Parser.string("RIGHT")
  val inner = Parser.string("INNER").as(JoinType.Inner)
  val outer = Parser.string("OUTER").as(JoinType.Outer)
  val join = Parser.string("JOIN").void
  val through = Parser.string("THROUGH").void
  val outgoing: Parser[Direction] = Parser.string("OUTGOING").as(Direction.Outgoing)
  val edge = Parser.string("EDGE").void
  val or = Parser.string("OR").void
  val and = Parser.string("AND").void
  val dot = Parser.char('.').void
  val pipe = Parser.char('|').soft.surroundedBy(whitespaces0).void
  val plus = Parser.string("+").as(Operator.Plus)
  val gte = Parser.string(">=").as(Operator.GreaterThanEqual)
  val equal = Parser.char('=').as(Operator.Equals)
  val lt = Parser.char('<').as(Operator.LessThan)
  val gt = Parser.char('>').as(Operator.GreaterThan)
  val sq = Parser.char('\'').void

  val ident: Parser[Expression.Ident] = alpha.repAs[String].map(s => Expression.Ident(Symbol(s)))

  val fieldAccessExpression: Parser[Expression] = Parser.recursive[Expression] { recurse =>
    // Parser for a dot followed by an expression
    val dotExpr: Parser[Expression] = (dot *> recurse)

    // Combine the parsers
    (ident ~ dotExpr.?).map {
      case (prefix, Some(suffix)) => Expression.BinOp(Operator.Dot, prefix, suffix)
      case (prefix, None)         => prefix
    }
  }

  val strchr = Parser.oneOf(alpha :: wsp :: Nil)

  val stringLit: Parser[Expression] = strchr.rep.string.between(sq, sq).map(v => Expression.Literal(Value.Text(v)))
  val intLit: Parser[Expression] = digit.rep.string.map(v => Expression.Literal(Value.Integral(v.toInt)))

  val op: Parser[Operator] = Parser.oneOf(gte :: plus :: equal :: lt :: gt :: Nil)

  val lhs = Parser.oneOf(stringLit :: intLit :: fieldAccessExpression :: ident :: Nil)

  val exp = Parser.recursive[Expression] { r =>

    val binOp: Parser[Expression] = (lhs $ (op $ r)).map(p => Expression.BinOp(p._2._1, p._1, p._2._2)).backtrack

    Parser.oneOf(binOp :: lhs :: Nil)
  }

  def opToString(op: Operator): String = op match {
    case Operator.Plus => "+"
    case Operator.Minus => ???
    case Operator.Dot => "."
    case Operator.Equals => ???
    case Operator.LessThan => ???
    case Operator.GreaterThan => ???
    case Operator.GreaterThanEqual => ???
    case Operator.And => ???
    case Operator.Or => ???
    case Operator.XOr => ???
    case Operator.Not => ???
  }

  def expToString(exp: Expression): String = exp match {
    case Expression.Literal(value) => value match {
      case Value.Null => "NULL"
      case Value.True => "true"
      case Value.False => "false"
      case Value.Integral(n) => n.toString
      case Value.Text(str) => s"'$str'"
    }
    case Expression.Ident(name) => name.name
    case Expression.Parameter(name) => name.name
    case Expression.Apply(name, args) => s"$name(${args.mkString(",")})"
    case Expression.UnaryOp(op, exp) => s"${opToString(op)}${expToString(exp)}"
    case Expression.BinOp(op, lhs, rhs) => s"(${expToString(lhs)} ${opToString(op)} ${expToString(rhs)})"
  }

  val aliasedProjection = (exp $ (as #> ident)).map(p => Projection.Calculation(p._1, p._2.name)).backtrack
  val defaultProjection = exp.map(e => Projection.Calculation(e, Symbol(expToString(e))))

  val projection = Parser.oneOf(aliasedProjection :: defaultProjection :: Nil)

  val selectClause = rep1(projection)

  val selectBit: Parser[NonEmptyList[Projection]] = (select *> selectClause)

  val upper = Parser.charWhere(_.isUpper)

  val label = (upper ~ alpha.rep0.string).map {
    case (first, rest) => first.toString ++ rest
  }

  val labels = label.repSep(pipe.soft.surroundedBy(whitespaces0)).surroundedBy(whitespaces0).backtrack

  val aliasedTable = (labels ~ ident).map { p =>
    Predicate.ExistsNode(p._2.name, p._1.toList.map(s => Symbol(s)))
  }
  val table = label.map { l =>
    Predicate.ExistsNode(CheatingNameGenerator.synthesizeIdent(), List(Symbol(l)))
  }

  val tableExp = Parser.oneOf(aliasedTable :: table :: Nil)

  val joinType: Parser[JoinType] = Parser.oneOf(inner :: outer :: Nil)

  val direction = Parser.oneOf(outgoing :: Nil)

  val edgePart: Parser[NonEmptyList[String]] = (edge #> labels)

  val pathKind: Parser[NonEmptyList[String]] = Parser.oneOf(edgePart :: Nil)

  val path: Parser[(Direction,NonEmptyList[String])] = (through #> (direction $ pathKind))

  val joinPart = (joinType $ (join #> (tableExp $ path)))

  val tablePart = (tableExp ~ joinPart.surroundedBy(whitespaces0).rep0).map { p =>
    val initName = p._1.binding
    p._2.foldLeft[(Predicate, Symbol)](p._1 -> initName) { (pred, token) =>
      val sourceName = pred._2
      val destName = token._2._1.binding
      val edgeExists = Predicate.ExistsEdge(CheatingNameGenerator.synthesizeIdent(), token._2._2._2.toList.map(name => Symbol(name)), sourceName, destName)
      val newPred = Predicate.And(pred._1, edgeExists)
      newPred -> destName
    }._1
  }

  val fromBit = from *> rep1(tablePart)

  val whereClause = Parser.recursive[Predicate] { r =>

    val inner = exp.map(e => Predicate.Satisfies(e))
    val orExps: Parser[Predicate] = (inner $ (or #> r)).map(p => Predicate.Or(p._1, p._2)).backtrack
    val andExps = (inner $ (and #> r)).map(p => Predicate.And(p._1, p._2)).backtrack

    Parser.oneOf(orExps :: andExps :: inner :: Nil)
  }

  val whereBit = where #> whereClause.surroundedBy(whitespaces0)

  val query =
    (selectBit.surroundedBy(whitespaces0) ~
      fromBit.surroundedBy(whitespaces0) ~
      whereBit.?.surroundedBy(whitespaces0)).map { p =>
      Query.Single(
        predicate = Predicate.And(p._1._2.toList.foldLeft(Predicate.sat)((prev,curr) => Predicate.And(prev, curr)), p._2.getOrElse(Predicate.True)),
        effects = List.empty[Effect],
        maybeProjection = Some(Projection.Parallel(p._1._1.toList))
      )
    }

  private def rewriteQuery(q: Query, r: Rewriter): Query = q match {
    case Query.Union(lhs, rhs) => Query.Union(rewriteQuery(lhs, r), rewriteQuery(rhs, r))
    case Query.Single(predicate, effects, maybeProjection) =>
      Query.Single(
        r.rewrite(predicate),
        effects.map {
          case Effect.Set(lhs, rhs) => Effect.Set(BooleanExpressionRewriter.rewrite(lhs), BooleanExpressionRewriter.rewrite(rhs))
          case Effect.Create(patternText) => Effect.Create(patternText)
        },
        maybeProjection
      )
    case Query.Empty => Query.Empty
  }

  def parseSql(sql: String, rewriters: List[Rewriter] = Nil): Query = {
    val parsedQuery: Query = query.parseAll(sql) match {
      case Left(err) => throw new RuntimeException(err.toString)
      case Right(q) => q
    }

    rewriters.foldRight(parsedQuery)((r, q) => rewriteQuery(q, r))
  }
}
