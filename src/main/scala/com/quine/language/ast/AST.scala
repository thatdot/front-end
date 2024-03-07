package com.quine.language.ast

import java.util.UUID

sealed trait Source

object Source {
    case class TextSource(start: Int, end: Int, text: String) extends Source
    case object NoSource extends Source
}


sealed trait Operator

object Operator {
    case object Plus extends Operator
    case object Minus extends Operator
    case object Dot extends Operator
    case object Equals extends Operator
    case object LessThan extends Operator
    case object GreaterThan extends Operator
    case object GreaterThanEqual extends Operator
    case object And extends Operator
    case object Or extends Operator
    case object XOr extends Operator
    case object Not extends Operator
}

sealed trait Value

object Value {
    case object Null extends Value
    case object True extends Value
    case object False extends Value
    case class Integral(n: Int) extends Value
    case class Text(str: String) extends Value
}

sealed trait Expression {
    val source: Source
}

object Expression {
    case class Literal(source: Source, value: Value) extends Expression
    case class Ident(source: Source, name: Symbol) extends Expression
    case class Parameter(source: Source, name: Symbol) extends Expression
    case class Apply(source: Source, name: Symbol, args: List[Expression]) extends Expression
    case class UnaryOp(source: Source, op: Operator, exp: Expression) extends Expression
    case class BinOp(source: Source, op: Operator, lhs: Expression, rhs: Expression) extends Expression

    def mkLiteral(source: Source, value: Value): Expression = Literal(source, value)
}

sealed trait Predicate {
    val source: Source
}

object Predicate {
    case class ExistsNode(source: Source, binding: Symbol, labels: List[Symbol]) extends Predicate
    case class ExistsEdge(source: Source, name: Symbol, labels: List[Symbol], sourceNode: Symbol, destNode: Symbol) extends Predicate
    case class ExistsPath(source: Source, name: Symbol, sourceNode: Symbol, destNode: Symbol) extends Predicate

    case class And(source: Source, lhs: Predicate, rhs: Predicate) extends Predicate
    case class Or(source: Source, lhs: Predicate, rhs: Predicate) extends Predicate
    case object True extends Predicate {
        val source: Source = Source.NoSource
    }
    case object False extends Predicate {
        val source: Source = Source.NoSource
    }

    case class Satisfies(source: Source, pexp: Expression) extends Predicate

    def sat: Predicate = True
}

sealed trait Projection

object Projection {
    case class Parallel(projections: List[Projection]) extends Projection
    case class Calculation(expression: Expression, as: Symbol) extends Projection
}

sealed trait Effect

object Effect {
    case class Set(lhs: Expression, rhs: Expression) extends Effect
    case class Create(patternText: String) extends Effect
}

sealed trait Query {
    val source: Source
}

object Query {
    case class Union(source: Source, lhs: Query, rhs: Query) extends Query
    case class Single(source: Source, predicate: Predicate, effects: List[Effect], maybeProjection: Option[Projection]) extends Query
    case object Empty extends Query {
        val source: Source = Source.NoSource
    }

    def empty: Query = Empty
}
