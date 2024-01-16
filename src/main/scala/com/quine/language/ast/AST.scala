package com.quine.language.ast

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

sealed trait Expression

object Expression {
    case class Literal(value: Value) extends Expression
    case class Ident(name: Symbol) extends Expression
    case class Parameter(name: Symbol) extends Expression
    case class Apply(name: Symbol, args: List[Expression]) extends Expression
    case class UnaryOp(op: Operator, exp: Expression) extends Expression
    case class BinOp(op: Operator, lhs: Expression, rhs: Expression) extends Expression

    def mkLiteral(value: Value): Expression = Literal(value)
}

sealed trait Predicate

object Predicate {
    case class ExistsNode(binding: Symbol, labels: List[Symbol]) extends Predicate
    case class ExistsEdge(name: Symbol, labels: List[Symbol], sourceNode: Symbol, destNode: Symbol) extends Predicate
    case class ExistsPath(name: Symbol, sourceNode: Symbol, destNode: Symbol) extends Predicate

    case class And(lhs: Predicate, rhs: Predicate) extends Predicate
    case class Or(lhs: Predicate, rhs: Predicate) extends Predicate
    case object True extends Predicate
    case object False extends Predicate

    case class Satisfies(pexp: Expression) extends Predicate

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

sealed trait Query

object Query {
    case class Union(lhs: Query, rhs: Query) extends Query
    case class Single(predicate: Predicate, effects: List[Effect], maybeProjection: Option[Projection]) extends Query
    case object Empty extends Query

    def empty: Query = Empty
}
