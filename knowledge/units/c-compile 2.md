As you begin C programming we ask you to use several command-line flags that cause the C compiler to be especially careful.
Specifically, you should compile every program like this:

```bash
gcc -Wall -pedantic -std=c11 -ggdb  program.c -o program
```

* `-Wall` turns on "all" possible warnings (`-W`), to help warn you of possible mistakes
* `-pedantic`, to be extra picky about syntax, again, to help avoid mistakes;
* `-std=c11`, to insist that your code follow the 'c11' version of the C language standard, and
* `-ggdb` to enable the resulting program to be debugged by the `gdb` debugger (more on that later).

To make this easier for you, our customized bash configuration defines an "alias":

```bash
alias mygcc='gcc -Wall -pedantic -std=c11 -ggdb'
```

which creates a new bash command `mygcc`, so the above can just be typed

```bash
mygcc  program.c -o program
```

Here you ask gcc to compile `program.c` and to output (`-o`) to file `program`.

> A word of warning: Whether using `gcc -o hello hello.c` or `mygcc -o hello hello.c` you must take care to avoid getting the order of the files wrong with the `-o` switch which tells the complier that the name of the file following the `-o` switch will be the name of the executable.
> One student compiled the correct way `mygcc -o hello hello.c` (producing a executable `hello` correctly) and then recompiled but got the order wrong: `mygcc -o hello.c hello`.
> What the `gcc` compiler did wasn't pleasant.
> It took the executable `hello` as the source file and and `hello.c` as the name of the executable to be created.
> The result was the real source file `hello.c` disappeared!
> Well, it didn't actually disappear, it was just erased by the compiler as it got ready to produce a new output file of that same name.
> So please be careful: the `-o` tells the compiler that the executable it creates should be given the name that follows the `-o`.

Because `mygcc` is a bash alias, it is only available at the bash commandline.
If you want to compile your program elsewhere, e.g., from within emacs, you'll need to type out the full commandline above.

The compilation of a C program actually requires several steps: preprocessing, compiling, assembling, and linking, as shown below. 
We will return to this diagram as we learn more about C, and are better able to understand the purpose of each step.

![](media/c-compile/compilation.png)

In this diagram, I envision compiling a program `names.c` and linking it with the module `readlinep.c`.
When we run the compiler like this:

```bash
$ mygcc names.c readlinep.c -o names
```

It is actually running a series of commands, creating (and later removing) various intermediate files:

* run the C preprocessor `cpp` on `names.c` to produce `names.i`: still C source code, but with comments removed and with `#include` files incorporated.
* run the C compiler `cc` on `names.i` to produce assembly language `names.s`. This is still a text file, but no longer in C.
*  run the assembler `as` on `names.s` to produce the object code `names.o`.  This is now a binary file containing machine instructions.
*  repeat those steps for `readlinep.c`, resulting in `readlinep.o`.
*  run the linker `ld` on `names.o` and `readlinep.o`, linking them together and with common libraries like `stdio.a`, to produce an executable binary program `names`.

We use `gcc`, the Gnu C compiler, which may actually use Gnu versions of the above programs (like `gcc` instead of `cc` and `gas` instead of `gas`) for some steps.


Here's a video describing the process of compiling *and linking* these modules together:

**[:arrow_forward: Compilation process video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c4bd1000-66ca-4557-a46e-acb100003d81)**

In the accompanying demo, I write, compile, assemble, and link a simpler program `play.c`.

> At one point (4:25) I misspoke, saying "I'm going to run the assembler" when I should have said "I'm going to run the compiler to produce assembly code".
