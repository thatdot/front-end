package com.quine.cypher

import com.quine.cypher.parsing.{CypherLexer, CypherParser}
import com.quine.cypher.phases.{BooleanExpressionRewriter, Rewriter}
import com.quine.cypher.visitors.QueryVisitor
import com.quine.language.ast.{Effect, Projection, Query}
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

protected class Parser(rewriters: List[Rewriter]) {

  private def rewriteProjection(projection: Projection): Projection = projection match {
    case Projection.Parallel(projections) => Projection.Parallel(projections.map(rewriteProjection))
    case Projection.Calculation(expression, as) => Projection.Calculation(BooleanExpressionRewriter.rewrite(expression), as)
  }

  private def rewriteQuery(q: Query, r: Rewriter): Query = q match {
    case Query.Union(source, lhs, rhs) => Query.Union(source, rewriteQuery(lhs, r), rewriteQuery(rhs, r))
    case Query.Single(source, predicate, effects, maybeProjection) =>
      Query.Single(
        source,
        r.rewrite(predicate),
        effects.map {
          case Effect.Set(lhs, rhs) => Effect.Set(BooleanExpressionRewriter.rewrite(lhs), BooleanExpressionRewriter.rewrite(rhs))
          case Effect.Create(patternText) => Effect.Create(patternText)
        },
        maybeProjection.map(p => rewriteProjection(p))
      )
    case Query.Empty => Query.Empty
  }

  def parseCypher(cypherText: String): Either[List[ParseError], Query] = {
    val errorListener = new CollectingErrorListener

    val input = CharStreams.fromString(cypherText)
    val lexer = new CypherLexer(input)

    val tokens = new CommonTokenStream(lexer)
    val parser = new CypherParser(tokens)

    lexer.removeErrorListeners()
    parser.removeErrorListeners()

    lexer.addErrorListener(errorListener)
    parser.addErrorListener(errorListener)

    val tree = parser.oC_Query()

    val maybeSyntaxTree = QueryVisitor.visitOC_Query(tree)
    maybeSyntaxTree.map(syntaxTree => rewriters.foldRight(syntaxTree)((r, q) => rewriteQuery(q, r))) match {
      case Some(q) => Right(q)
      case None => Left(errorListener.errors.toList)
    }
  }
}
