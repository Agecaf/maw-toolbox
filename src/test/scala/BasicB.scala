import org.scalatest._
import com.agecaf.mawtoolbox.SmallCompiler._

class BasicB extends FlatSpec with Matchers {

  behavior of "Toolbox"

  it should "have allow external code to use internal code." in {

    import com.agecaf.mawtoolbox.testbasicb.Hi

    val program = parseAndCompile("""
      | import com.agecaf.mawtoolbox.testbasicb.Hi
      | val x = Hi
      | val y = x
      | y
      """.stripMargin)

    program() shouldBe Hi
  }
}

package com.agecaf.mawtoolbox.testbasicb {
  case object Hi
}
