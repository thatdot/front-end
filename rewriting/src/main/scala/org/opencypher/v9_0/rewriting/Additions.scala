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
package org.opencypher.v9_0.rewriting

import org.opencypher.v9_0.ast.CreateIndexNewSyntax
import org.opencypher.v9_0.ast.CreateNodeKeyConstraint
import org.opencypher.v9_0.ast.CreateNodePropertyExistenceConstraint
import org.opencypher.v9_0.ast.CreateRelationshipPropertyExistenceConstraint
import org.opencypher.v9_0.ast.CreateUniquePropertyConstraint
import org.opencypher.v9_0.ast.DropConstraintOnName
import org.opencypher.v9_0.ast.DropIndexOnName
import org.opencypher.v9_0.ast.Statement
import org.opencypher.v9_0.expressions.ExistsSubClause
import org.opencypher.v9_0.util.CypherExceptionFactory

object Additions {

  // This is functionality that has been added in 4.0 and 4.1 and should not work when using CYPHER 3.5
  case object addedFeaturesIn4_x extends Additions {

    override def check(statement: Statement, cypherExceptionFactory: CypherExceptionFactory): Unit = statement.treeExists {

      // CREATE INDEX [name] FOR (n:Label) ON (n.prop)
      case c: CreateIndexNewSyntax =>
        throw cypherExceptionFactory.syntaxException("Creating index using this syntax is not supported in this Cypher version.", c.position)

      // DROP INDEX name
      case d: DropIndexOnName =>
        throw cypherExceptionFactory.syntaxException("Dropping index by name is not supported in this Cypher version.", d.position)

      // CREATE CONSTRAINT name ON ... IS NODE KEY
      case c@CreateNodeKeyConstraint(_, _, _, Some(_), _) =>
        throw cypherExceptionFactory.syntaxException("Creating named node key constraint is not supported in this Cypher version.", c.position)

      // CREATE CONSTRAINT name ON ... IS UNIQUE
      case c@CreateUniquePropertyConstraint(_, _, _, Some(_), _) =>
        throw cypherExceptionFactory.syntaxException("Creating named uniqueness constraint is not supported in this Cypher version.", c.position)

      // CREATE CONSTRAINT name ON () ... EXISTS
      case c@CreateNodePropertyExistenceConstraint(_, _, _, Some(_), _) =>
        throw cypherExceptionFactory.syntaxException("Creating named node existence constraint is not supported in this Cypher version.", c.position)

      // CREATE CONSTRAINT name ON ()-[]-() ... EXISTS
      case c@CreateRelationshipPropertyExistenceConstraint(_, _, _, Some(_), _) =>
        throw cypherExceptionFactory.syntaxException("Creating named relationship existence constraint is not supported in this Cypher version.", c.position)

      // DROP CONSTRAINT name
      case d: DropConstraintOnName =>
        throw cypherExceptionFactory.syntaxException("Dropping constraint by name is not supported in this Cypher version.", d.position)

      case e: ExistsSubClause =>
        throw cypherExceptionFactory.syntaxException("Existential subquery is not supported in this Cypher version.", e.position)

      // Administration commands against system database are checked in CompilerFactory to cover all of them at once
    }
  }

  // This is functionality that has been added in 4.2 and should not work when using CYPHER 3.5 and CYPHER 4.1
  case object addedFeaturesIn4_2 extends Additions {

    override def check(statement: Statement, cypherExceptionFactory: CypherExceptionFactory): Unit = {}
  }
}

trait Additions extends {
  def check(statement: Statement, cypherExceptionFactory: CypherExceptionFactory): Unit
}
