// First attempt at using scala.meta with a toolbox.
// Fails.

import org.scalatest._
import com.agecaf.mawtoolbox.SmallCompiler._

class MacrosMetaA extends FlatSpec with Matchers {

  behavior of "Toolbox & Scala.meta"

  it should "evaluate a simple program." in {

    val program = parseAndCompile("""
      | import com.agecaf.mawtoolbox.macros.metaA._
      | var x = 0
      | var y = 0
      | @reverse def doThings() = {
      |   y = 2   // Executed third
      |   x = y   // Executed second
      |   y = 1   // Executed first
      | }
      | doThings()
      | return s"x = $x, y = $y"
      """.stripMargin)

    program() shouldBe "x = 1, y = 2"
  }
}

package com.agecaf.mawtoolbox.macros.metaA {
  import scala.meta._
  class reverse extends scala.annotation.StaticAnnotation {
    inline def apply(defn: Any): Any = meta {
      val q"$mods def $methodName[..$tpes](...$args): $returnType = { ..$body }" = defn

      q"$mods def $methodName[..$tpes](...$args): $returnType = { ..${body.reverse} }"
    }
  }
}

/*
Fails with

[info] - should evaluate a simple program. *** FAILED ***
[info]   scala.tools.reflect.ToolBoxError: reflective compilation has failed:
[info]
[info] macro annotation could not be expanded (the most common reason for that is that you need to enable the macro paradise plugin; another possibility is that you try to use macro annotation in the same compilation run that defines it)

So... Can we somehow set the plugins for our toolbox?
Or is it that scala.meta cannot be used within the same project
like def macros can (see MacrosDefA)?

*/
