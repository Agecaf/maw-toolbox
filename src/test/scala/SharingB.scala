// Second attempt at sharing, via a global provided object.

import org.scalatest._
import com.agecaf.mawtoolbox.SmallCompiler._

class SharingB extends FlatSpec with Matchers {
  import com.agecaf.mawtoolbox.sharing.sharingb._

  behavior of "Toolbox & Global object"

  it should "Share a common value." in {

    val source = parseAndCompile("""
      | import com.agecaf.mawtoolbox.sharing.sharingb._
      | Lib.lib += 'Hello -> "World"
      | Lib.lib += 'pt1 -> Vec2(0, 0)
      """.stripMargin)

    source()

    val program = parseAndCompile("""
      | import com.agecaf.mawtoolbox.sharing.sharingb.Lib
      | (Lib.lib('Hello), Lib.lib('pt1))
      """.stripMargin)

    program() shouldBe (("World", Vec2(0, 0)))
  }

  it should "Share an object." in {

    /*
    In this example, we pass an object. To use its methods,
    we have to use some reflection though, which is inconvenient.
    */

    val source = parseAndCompile("""
      | import com.agecaf.mawtoolbox.sharing.sharingb.Lib
      | object Foo {
      |   val bar = "baz"
      | }
      | Lib.lib += 'Foo -> Foo
      | Foo     // To print what object does Foo refer to.
      """.stripMargin)

    source()

    // println(source()) // This gives something like:
    // __wrapper$1$58f34dd71e554f0898674fd976d76b5c.__wrapper$1$58f34dd71e554f0898674fd976d76b5c$Foo$2$@25511156

    val program = parseAndCompile("""
      | import com.agecaf.mawtoolbox.sharing.sharingb.Lib
      | val Foo = Lib.lib('Foo).asInstanceOf[{val bar: String}]
      | Foo.bar
      """.stripMargin)

    program() shouldBe "baz"
  }

  it should "Share a function with internal and standar types." in {

    /*
    Here's an example of sharing a function whose signature we know.
    Note it includes an internal type; Vec2.
    */
    val source = parseAndCompile("""
      | import com.agecaf.mawtoolbox.sharing.sharingb._
      | Lib.lib += 'twice -> {x: Double => Vec2(x, x)}
      """.stripMargin)

    source()

    val program = parseAndCompile("""
      | import com.agecaf.mawtoolbox.sharing.sharingb._
      | val twice = Lib.lib('twice).asInstanceOf[Double => Vec2]
      | twice(1)
      """.stripMargin)

    program() shouldBe Vec2(1, 1)
  }

  it should "Share objects via an internal interface rather than introspection." in {

    /*
    Here we use an internal interface (Greeter) to allow the creation and
    use of custom objects in external code.
    */
    val source = parseAndCompile("""
      | import com.agecaf.mawtoolbox.sharing.sharingb._
      | object Hello extends Greeter {
      |   def greet(name: String) = s"Hello, $name!"
      | }
      | Lib.lib += 'HelloObj -> Hello
      """.stripMargin)

    source()

    val program = parseAndCompile("""
      | import com.agecaf.mawtoolbox.sharing.sharingb._
      | val Hello = Lib.lib('HelloObj).asInstanceOf[Greeter]
      | Hello greet "World"
      """.stripMargin)

    program() shouldBe "Hello, World!"
  }
}

package com.agecaf.mawtoolbox.sharing.sharingb {

  /** Mutable Global object which allows to share code
    *  among different External codes.
    */
  object Lib {
    var lib = Map[Symbol, Any]()
  }

  /** Simple class to be useable in the external code.
    */
  final case class Vec2(x: Double, y: Double) {
    def +(other: Vec2) =
      Vec2(x + other.x, y + other.y)
  }

  /** Simple class to be extended and used as interface in
    * the external code.
    */
  abstract class Greeter {
    def greet(name: String): String
  }
}
