package com.quine.cypher.utils

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor

import cats.implicits._

import collection.JavaConverters._

object Helpers {
  def maybeMatch[A](ctx: ParserRuleContext, visitor: AbstractParseTreeVisitor[A]): Option[A] =
    if(ctx == null) Option.empty[A] else Some(ctx.accept(visitor))

  def maybeMatchList[A,B <: ParserRuleContext](ctx: java.util.List[B], visitor: AbstractParseTreeVisitor[A]): Option[List[A]] =
    if(ctx == null) Option.empty[List[A]] else ctx.asScala.toList.traverse(inner => maybeMatch(inner, visitor))
}
