Recall the [unit](https://github.com/CS50Dartmouth21FS1/home/blob/main/knowledge/units/c-compile.md) on the C compilation process; the very first step of translating any C source file is to pass it through the C *preprocessor*.

The preprocessor understands various *directives*, all beginning with `#`.
We've already seen one common directive, `#include`.
Another common use is for *conditional compilation*, that is, to include (or exclude) a section of code based on a condition - most commonly, whether or not a given preprocessor symbol is defined.

## DEBUG

One use of preprocessor *conditional compilation* is to support debugging.
Consider this very common example:

```c
#define DEBUG
...
#ifdef DEBUG
 ... code for use when DEBUG is defined
#else // DEBUG
 ... code for use when DEBUG is not defined
#endif // DEBUG
```

The C preprocessor literally includes some code, and strips out other code, before passing it to the C compiler.
In the above example, we define the preprocessor constant called `DEBUG`, and later test its value with `#ifdef`.
Notice the use of comments on the `#else` and `#endif` lines to help readability.

The above example is a common approach for debugging.
Even better, remove the `#define DEBUG` line and let that be determined by the programmer at compile time:

```c
#ifdef DEBUG
 ... code for use when DEBUG is defined
#else // DEBUG
 ... code for use when DEBUG is not defined
#endif // DEBUG
```

```bash
  mygcc -DDEBUG program.c -o program
```

The program compiles (and behaves) differently with and without the compile-time switch `-DDEBUG`.
This particular approach is very common and vastly better than editing the code to add/remove debugging code, or to comment in/out the debugging code; just flip a switch on the commandline!

## MEMTEST

In CS50 you'll see a specific example of conditional compilation in support of debugging.
It is in the `bag` module, part of the starter kit for Labs 3-6.
For example,

```c
bag_insert(bag_t* bag, void* item)
{
...

#ifdef MEMTEST
  mem_report(stdout, "After bag_insert");
#endif
}
```

When `-DMEMTEST` is added to the gcc command-line, to define the preprocessor symbol `MEMTEST`, the call to `mem_report()` becomes part of the compiled code; otherwise it does not.
The source file never changes - but the person compiling the source file can 'flip a switch' to enable this debugging code when needed.

## Header files

Another common use of conditional compilation is to protect against repeated inclusion of a header file.
See the [unit about header files](https://github.com/CS50Dartmouth21FS1/home/blob/main/knowledge/units/headerfiles.md) for more info.
