import org.scalatest._
import com.agecaf.mawtoolbox.SmallCompiler._

class MacrosDefA extends FlatSpec with Matchers {

  behavior of "Toolbox & Macros"

  it should "allow external code to use macros" in {

    /*
    The swap method is a simple macro which swaps the execution of
    statements for demonstration purpose.

    In this case, we swap two assigments so that one occurs before the other.
    I believe this is not possible without macros, so I guess this is a
    good enough example.
    */
    val program = parseAndCompile("""
      | import com.agecaf.mawtoolbox.macros.DefMacrosA.swap
      | var x = 0
      | var y = 0
      | swap {
      |   y = 2   // Executed third
      |   x = y   // Executed second
      |   y = 1   // Executed first
      | }
      | return s"x = $x, y = $y"
      """.stripMargin)

    program() shouldBe "x = 1, y = 2"
  }
}

package com.agecaf.mawtoolbox.macros {
  object DefMacrosA {
    import scala.language.experimental.macros
    import scala.reflect.macros.Context

    /*
    Simple macro which reverses the execution of the given statements.
    Serves as demonstration purpose as I believe this cannot be done
    without macros.
    */
    def swap(in: Unit): Unit = macro swapImpl
    def swapImpl(c: Context)(in: c.Expr[Unit]): c.Expr[Unit] = {
      import c.universe._
      val out = in.tree match {
        case q"{..$stats}" => q"""{..${stats.reverse}}"""
        case _ => q"""throw(new Throwable("Could not match macro"))"""
      }
      c.Expr(out)
    }
  }
}
