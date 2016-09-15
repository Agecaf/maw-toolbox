// Naive attempt at having external code use other external code.
// Via defining packages from meta code.
// Fails.

import org.scalatest._
import com.agecaf.mawtoolbox.SmallCompiler._

class SharingA extends FlatSpec with Matchers {

  behavior of "Toolbox"

  it should "evaluate a simple program." in {

    val source = parseAndCompile("""
      | package com.agecaf.mawtoolbox.external.basicc {
      |   //case class Hello(name: String)
      | }
      | 2
      """.stripMargin)

    // source() ¿?

    val program = parseAndCompile("""
      | import com.agecaf.mawtoolbox.external.basicc.Hello
      | val x = Hello("world")
      | return x.toString
      """.stripMargin)

    println(program())
    program() shouldBe "Hello(world)"
  }
}


/*
Fails with the following error.

[info] BasicC:
[info] Toolbox
[info] - should evaluate a simple program. *** FAILED ***
[info]   java.lang.AssertionError: assertion failed: method wrapper
[info]   at scala.reflect.internal.Symbols$Symbol.newPackage(Symbols.scala:310)
[info]   at scala.tools.nsc.typechecker.Namers$Namer.createPackageSymbol(Namers.scala:381)
[info]   at scala.tools.nsc.typechecker.Namers$Namer.createPackageSymbol(Namers.scala:374)
[info]   at scala.tools.nsc.typechecker.Namers$Namer.createPackageSymbol(Namers.scala:374)
[info]   at scala.tools.nsc.typechecker.Namers$Namer.createPackageSymbol(Namers.scala:374)
[info]   at scala.tools.nsc.typechecker.Namers$Namer.createPackageSymbol(Namers.scala:374)
[info]   at scala.tools.nsc.typechecker.Namers$Namer.assignSymbol(Namers.scala:308)
[info]   at scala.tools.nsc.typechecker.Namers$Namer.enterPackage(Namers.scala:688)
[info]   at scala.tools.nsc.typechecker.Namers$Namer.dispatch$1(Namers.scala:283)
[info]   at scala.tools.nsc.typechecker.Namers$Namer.standardEnterSym(Namers.scala:299)
[info]   ...

Apparently the compiler is trying to do create the package as
naively intended, but faces an assertion when trying to do that.

The code throwing the assertion error is apparently the following:

  final def newPackage(name: TermName, pos: Position = NoPosition, newFlags: Long = 0L): ModuleSymbol = {
     assert(name == nme.ROOT || isPackageClass, this)
     newModule(name, pos, PackageFlags | newFlags)
   }

Most likely the Toolbox is *not* intended to be used in this way?
*/
