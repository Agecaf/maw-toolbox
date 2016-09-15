import org.scalatest._
import com.agecaf.mawtoolbox.SmallCompiler._

class BasicA extends FlatSpec with Matchers {

  behavior of "Toolbox"

  it should "evaluate a simple program." in {

    val program = parseAndCompile("""
      | val x = 2
      | x
      """.stripMargin)

    program() shouldBe 2
  }
}
