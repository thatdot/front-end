/*
 * Copyright © 2002-2020 Neo4j Sweden AB (http://neo4j.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencypher.v9_0.expressions.functions

// this implementation exists only to handle the case where "reduce(x = 0, x in y : foo)" is parsed as a function invocation,
// rather than a ReduceExpression
case object Reduce extends Function with FunctionWithInfo {
  def name = "reduce"

  // TODO: Get specification formalized by CLG
  override def getSignatureAsString: String =
    name + "(accumulator :: VARIABLE = initial :: ANY?, variable :: VARIABLE IN list :: LIST OF ANY? | expression :: ANY) :: (ANY?)"

  override def getDescription: String = "Runs an expression against individual elements of a list, storing the result of the expression in an accumulator."
}
