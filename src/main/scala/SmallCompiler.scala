package com.agecaf.mawtoolbox

object SmallCompiler {
  /** Parses and compiles a string, returning the compiled program.
    *
    * Inspired by http://stackoverflow.com/questions/23874281/scala-how-to-compile-code-from-an-external-file-at-runtime
    */
  def parseAndCompile(src: String): () => Any = {
    // Get Mirror and toolbox.
    import scala.reflect.runtime.currentMirror
    import scala.tools.reflect.ToolBox
    val toolbox = currentMirror.mkToolBox()
    import toolbox.u._

    // Parse the source into a tree.
    val tree = toolbox.parse(src)

    // Compile tree with toolbox.
    val compiled = toolbox.compile(tree)

    // Return compiled code.
    compiled
  }
}
