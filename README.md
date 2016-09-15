# Messing Around With: Toolbox
Here's some tests to explore my understanding of Scala's toolbox for runtime compilation. I'll also explore its interaction with scala macros and scala.meta.

### Running

Just use `sbt test`, or `sbt testOnly BasicA` for running a single example.

### Notation

- Internal code -> Code written and compiled normally.
- External code -> Code compiled and used at runtime.

# Answered Questions

### Can I compile and run scala code at runtime (external code)?
Yes. See BasicA.

We use a Toolbox to do the parsing and compilation.

### Can I use internal code in external code?
Yes. See BasicB.

I'll investigate whether this depends on the choice of mirror and universe, if that is possible.

### Can I use def macros in external code?
Yes. See MacrosDefA.

In fact, with this it is fairly simple to test macros within the macros project. A first downside is that you effictively recompile the macrotest every time the tests are run, but that's probably what you should do anyway. The second downside is that you probably don't get IDE support when writing the macro tests.

# Pending Questions

### What are mirrors, can we change them?

The currently used runtime mirror seems to be the defacto mirros for runtime metaprogramming, while other mirror might be used to restrict the access external code has to internal code, though that needs more investigation.

### What are Context's, what Context's are being used?

This seems to be used in def macros but not scala.meta. I'm still uncertain about this.

### How will runtime compilation work in dotty, do we already know or is it still "to be decided"?

### What features of scala.meta can we use for runtime compilation?

Maybe inline macros?

### Can we do internal dynamic imports?

I'll investigate examples with this later on. Probably a runtime-compilation workaround can be used.

### What is scala paradise, and how can we use meta annotations in runtime compilation?

See MacrosMetaA. We seem to be able to do it, though we're facing an error. We either need to somehow tell the Toolbox to use the paradise plugin, or split the scala.meta code into a different thing.

### How do compiler plugins and runtime compilation interact?

This might answer our questions on MacrosMetaA.

### Can I define things in an external code, and then use them in a second external code?

Apparently not by using packages (See SharingA), but some workarounds using global internal objects seeem to be possible (See SharingB). Still working on external types.
