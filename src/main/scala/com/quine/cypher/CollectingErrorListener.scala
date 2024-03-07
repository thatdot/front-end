package com.quine.cypher

import org.antlr.v4.runtime.{BaseErrorListener, RecognitionException, Recognizer}
import org.antlr.v4.runtime.misc.ParseCancellationException

case class ParseError(line: Int, char: Int, message: String)

class CollectingErrorListener extends BaseErrorListener {
  val errors = scala.collection.mutable.ArrayBuffer.empty[ParseError]

  override def syntaxError(recognizer: Recognizer[_, _], offendingSymbol: Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
    val error = ParseError(line = line, char = charPositionInLine, message = msg)
    errors += error
  }

  def getErrors: Seq[ParseError] = errors.toSeq
}