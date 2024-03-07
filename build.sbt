import Dependencies._

ThisBuild / scalaVersion     := "2.13.12" // Replace x with the minor version you're targeting
ThisBuild / version          := "0.1.1-SNAPSHOT"
ThisBuild / organization     := "com.thatdot"
ThisBuild / organizationName := "quine"

javacOptions := Seq("-source", "11", "-target", "11")

libraryDependencies += "org.antlr" % "antlr4" % "4.9.2"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.3"

enablePlugins(Antlr4Plugin)

Antlr4 / antlr4PackageName := Some("com.quine.cypher.parsing")
Antlr4 / antlr4Version := "4.9.2"
Antlr4 / antlr4GenListener := false // default: true
Antlr4 / antlr4GenVisitor := true // default: false

lazy val root = (project in file("."))
  .settings(
    name := "query-language",
    libraryDependencies += "org.eclipse.lsp4j" % "org.eclipse.lsp4j" % "0.21.1",
    libraryDependencies += "org.typelevel" %% "cats-parse" % "0.3.7",
    libraryDependencies += munit % Test,
    publishMavenStyle := true,
    publishTo := Some(Resolver.url("FrugalMechanic Snapshots", url("s3://com.thatdot.dependencies/"))(Resolver.ivyStylePatterns))
  )
