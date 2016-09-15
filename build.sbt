
scalaVersion := "2.11.8"

autoCompilerPlugins := true

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.scalameta" %% "scalameta" % "1.1.0"
)

addCompilerPlugin("org.scalameta" % "paradise_2.11.8" % "3.0.0-M5")
