**[:arrow_forward: Video walk-through](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=85c16154-2f5b-46ce-9ed3-ad0d00fd8127)** of the resulting example.

So far we have been compiling C programs using `mygcc`, a bash alias defined as follows:

```bash
alias mygcc='gcc -Wall -pedantic -std=c11 -ggdb'
```

Thus, when you run the `mygcc` command you are actually running `gcc` with a set of CS50-standard switches.

After this unit, you'll never use `mygcc` again.

As our programs become more complex, it's increasingly tedious (and error-prone) to type the sequences of commands to compile the code into an executable.
Indeed, for a large complex code base, it can take minutes or hours to compile all the code.
Fortunately, there is a good tool to make an executable from a collection of source files, incrementally recompiling only those files that have been updated.

The `make` command reads a configuration file called `Makefile` -- written in its own little language -- and executes shell commands as needed to compile all the source files and link them into an executable.

From now on we will compile all our programs with `make`.

Before we look at a specific example, let's first take another look at the compilation process (recalling the earlier [unit](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/c-compile.md)).

### Compiling and linking

The *compiler* reads "source code" (from a file like `bag.c` containing C code) and outputs "assembly code" (in a file `bag.s` containing assembly language).
Assembly language is still a human-readable text file, but now lists machine-level instructions instead of C statements.
In other words, the compiler translates source code into assembly code.
The traditional Unix command for the C compiler is `cc`, though we use one from Gnu called `gcc`.

The *assembler* reads assembly code and outputs "object code", translating the textual machine instructions into binary machine instructions.
It's still not a complete program, not yet "executable" by the operating system.
The Unix command for the assembler is `as`.

> The word "object" here has nothing to do with object-oriented programming, a la Java.
> Instead, think of this sentence: "The C source code in `foo.c` is compiled into machine instructions in `foo.o`."
> The *subject* of this sentence is the "source code", and the *object* of the sentence is the "machine instructions".

The *linker* reads object code (from files like `sorter.o`, `bag.o`, and `readlinep.o`, and from libraries) and links them all together into an "executable" file, ready for the OS to run (execute) when we ask bash to run it.
It does not translate the code further -- both input and output are machine instructions in a binary format -- it "links" the pieces together, resolving references in one file to functions and variables in other files.
For example, `sorter.o` may reference a function `bag_new()`, which the linker finds in `bag.o`.
The linker assigns memory addresses for all the code and global variables, ready for loading into memory when the program is executed.
The Unix command for the linker is `ld` (short for "load", not to be confused with `ln`, which links files together).

The `gcc` command is actually pretty sophisticated, and in a form like

```bash
gcc sorter.c bag.c readlinep.c -o sorter
```

will actually run four commands, something like this:

```bash
gcc -c sorter.c
gcc -c bag.c
gcc -c readlinep.c
ld sorter.o bag.o readlinep.o libraries... -o sorter
rm sorter.o bag.o readlinep.o
```

Each of the first three ask to "compile" (`-c`) the listed C source file; by default, the compiler creates an object-code file in a file of the same name with the extension `.o`.
(Under the hood, it's actually running both the compiler and then the assembler, briefly creating the intermediate `.s` file.)
The fourth line links those three object files together with a collection of common libraries, to produce the executable program; the original `-o sorter` commandline argument is placed here.
Finally, it cleans up by removing the intermediate object files.

Again, for a large code base, it is a waste of effort to recompile every C source file every time, even if only one of them has changed since the last compilation.

### Dependencies

To optimize this process it helps to know which files depend on each other.
Clearly, each object file depends on its source file:

```makefile
sorter.o: sorter.c
bag.o: bag.c
readlinep.o: readlinep.c
```

and the program depends on all the object files that will linked together:

```makefile
sorter: sorter.o bag.o readlinep.o
```

> It also depends on the standard libraries, but we assume they never change and we won't worry about them.

Actually, if we look deeper, the object files also depend on the include files, because those files are virtually included in the C source files -- the compiler reads the include file `bag.h` when it sees `#include "bag.h"` at some point while reading the C source code.
We need to communicate that fact to `make`.
Thus, we also have dependencies

```makefile
sorter.o: bag.h readlinep.h
bag.o: bag.h
readlinep.o: readlinep.h
```

> These modules' dependencies also include `.h` files for standard libraries, but again, we assume those never change and thus we don't worry about them as dependencies.

The above dependencies are actually written in `make` language.
The name to the left of each colon is the *target*, and the name(s) to the right of the colon are the *dependencies*.
The target depends on the dependencies, and should be rebuilt if any of the dependencies change.
Notice that dependencies are transitive: `sorter` depends on `bag.o` which depends on `bag.c` and `bag.h`, and so forth.
Make builds the *dependency tree* and rebuilds files as needed.

But how does it know how to rebuild one file from another?
We add a command (or list of commands), indented with a tab, on the line(s) that immediately follow the target.
We put all this together in a file called `Makefile`:

```makefile
# executable depends on object files
sorter: sorter.o bag.o readlinep.o
	gcc sorter.o bag.o readlinep.o -o sorter

# object files depend on source files
sorter.o: sorter.c
bag.o: bag.c
readlinep.o: readlinep.c

# object files also depend on include files
sorter.o: bag.h readlinep.h
bag.o: bag.h
readlinep.o: readlinep.h

# how to compile source into object files
sorter.o:
	gcc -c sorter.c
bag.o:
	gcc -c bag.c
readlinep.o:
	gcc -c readlinep.c
```

> Important: commands must be indented with a *tab* character, not spaces, or Make will not recognize them as commands.
Some editors are savvy and insert a tab when you type a tab, and some try to be clever and change the tab to spaces.
Make requires a tab.

Now, all we have to do is type `make`, and let it do all the work!

```bash
$ ls
Makefile  bag.c  bag.h  readlinep.c  readlinep.h  sorter.c
$ make
cc    -c -o sorter.o sorter.c
cc    -c -o bag.o bag.c
cc    -c -o readlinep.o readlinep.c
cc  sorter.o bag.o readlinep.o -lm -o sorter
$ ls
Makefile  bag.h  readlinep.c  readlinep.o  sorter.c
bag.c     bag.o  readlinep.h  sorter*      sorter.o
$ make
make: 'sorter' is up to date.
$ touch bag.h
$ make
gcc -c sorter.c
gcc -c bag.c
gcc sorter.o bag.o readlinep.o -o sorter
$ ls
Makefile  bag.h  readlinep.c  readlinep.o  sorter.c
bag.c     bag.o  readlinep.h  sorter*      sorter.o
$ 
```

Notice that, the first time I ran `make` it compiled each source file into an object file, and then compiled them all together.
(We use `gcc` to link object files, because it's very complicated to set up a proper `ld` command line, and `gcc` is smart enough to realize it need only run the linker when it's only given object files.)

On my second run of `make`, it checked the dependency tree and decided there was nothing to be rebuilt.
Time saved!

I pretended to edit `bag.h` by "touching" it - the `touch` command just updates the modification date of the file(s) named on its command line, creating files if needed but making no changes to the content of existing files.

On my third run of `make`, it checked the dependency tree and saw that both `bag.o` and `sorter.o` depend on `bag.h`, which had a newer modification date, so it recompiled both.
Because those two object files changed, it had to rebuild `sorter`.
In the end, notice that the `.o` files stick around - which is why `make` does not need to rebuild them every time.

Make actually knows about C and the relationship between `.c` and `.o` files, so several of the dependencies we listed were not necessary to list.
Indeed, some of the commands we listed are also unnecessary: they are *implicit rules* already known to make.
We thus streamline our `Makefile`:

```makefile
# executable depends on object files                                            
sorter: sorter.o bag.o readlinep.o
	gcc sorter.o bag.o readlinep.o -o sorter

# object files also depend on include files                                     
sorter.o: bag.h readlinep.h
bag.o: bag.h
readlinep.o: readlinep.h
```

Compare with the prior `Makefile` and you'll see two whole sections gone.
This new makefile works just the same; to see it, I'll start by cleaning up before I run `make`:

```bash
$ rm *.o sorter
rm: remove regular file 'bag.o'? y
rm: remove regular file 'readlinep.o'? y
rm: remove regular file 'sorter.o'? y
rm: remove regular file 'sorter'? y
$ make
cc    -c -o sorter.o sorter.c
cc    -c -o bag.o bag.c
cc    -c -o readlinep.o readlinep.c
gcc sorter.o bag.o readlinep.o -o sorter
$ make
make: 'sorter' is up to date.
$ ls
Makefile   bag.c  bag.o        readlinep.h  sorter*   sorter.o
Makefile~  bag.h  readlinep.c  readlinep.o  sorter.c
$ 
```

There are two important problems here, however.
First, you'll note that its implicit rules run `cc` instead of `gcc`.
Second, note that neither our explicit rules nor Make's implicit rules pass the CS50-standard command-line arguments to the C compiler.
We need to tell Make about those.
We can assign to some Make *variables* to inform Make about our preferred compiler and command-line options.
(Actually, Make calls them *macros* because their value cannot actually vary.)

```makefile
CC = gcc
CFLAGS = -Wall -pedantic -std=c11 -ggdb

# executable depends on object files
sorter: sorter.o bag.o readlinep.o
	$(CC) $(CFLAGS) sorter.o bag.o readlinep.o -o sorter

# object files also depend on include files
sorter.o: bag.h readlinep.h
bag.o: bag.h
readlinep.o: readlinep.h
```

The first two lines assign values to the macros `CC` and `CFLAGS`.
These two particular macros are known to Make; it will use them in its implicit rules.
To use them in our explicit rules, too, we have to refer to them explicitly - see the commands to rebuild `sorter`.
Notice that the macro-substitution syntax in Make uses a dollar sign - like bash - but requires parentheses around the macro name - unlike bash.
After removing all the derived files, run again:

```bash
$ make
gcc -Wall -pedantic -std=c11 -ggdb   -c -o sorter.o sorter.c
gcc -Wall -pedantic -std=c11 -ggdb   -c -o bag.o bag.c
gcc -Wall -pedantic -std=c11 -ggdb   -c -o readlinep.o readlinep.c
gcc -Wall -pedantic -std=c11 -ggdb sorter.o bag.o readlinep.o -o sorter
$ 
```
This time, Make used the `gcc` compiler and all our flags.

## Which target?

So far, we've just typed `make` at the command line.
We can actually tell Make to build a particular target by naming it:

```bash
$ make readlinep.o
gcc -Wall -pedantic -std=c11 -ggdb   -c -o readlinep.o readlinep.c
```

When given no target(s) on the command line, Make chooses the first target that has commands.
In our `Makefile`, that was `sorter` - which is why we put it first.
In a complex `Makefile`, it is common to place a target named `all` first, with a dependency (list) that indicates which target (list) is actually desired when one types `make all` or just `make`.

>  This is a 'phony target', because the result of `make all` is never to produce a file called `all`.
> That's ok - its purpose is to direct Make toward the target(s) we want it to build.

## Cleaning up

We've taught Make how to build our program – now let's teach it how to clean up.
By convention, every good `Makefile` has a target called `clean`.
Let's extend the above `Makefile` (by convention, the `clean` rule goes at the bottom of the file):

```makefile
clean:
	rm -f sorter
	rm -f *~ *.o
	rm -rf *.dSYM
```

The `-f` flag ("force") asks `rm` to exit with success even if it fails - e.g., if there are no files by those names to be removed.
The `-r` flag ("recursive") removes a directory and its contents.
These commands remove the `sorter` executable, and the object files `*.o`, and the emacs backup files `~`, and the dSYM directory `gcc` creates in support of `gdb`.

Look how well it cleans up!

```bash
$ ls
Makefile   bag.c  bag.o        readlinep.h  sorter*   sorter.o
Makefile~  bag.h  readlinep.c  readlinep.o  sorter.c
$ make clean
rm -f sorter
rm -f *~ *.o
rm -rf *.dSYM
$ ls
Makefile  bag.c  bag.h  readlinep.c  readlinep.h  sorter.c
$ 
```

In some Makefiles, you'll see the author explicitly tell Make that this rule is "phony":

```make
.PHONY: clean
```

This can also prevent it from being affected by a file that happens to be named `clean`!

## Testing

Another common/useful target is for automating a test run.
Let's add a phony target `test` that runs a test of `sorter`.

```makefile
# two quick test cases                                                          
test: sorter
	@echo "### 100 lines of input"
	sed 1,2d ~/cs50-dev/shared/examples/billboard.tsv | cut -f 3 | ./sorter
	@echo "### empty input"
	./sorter < /dev/null
	@echo "### done"
```

Notice that `test` depends on `sorter`, so `make test` will implicitly `make sorter` first if that program is out of date with respect to its dependencies (source code).

Notice that the rule for target `test` executes five commands; that's fine, a rule can contain a sequence of commands, each executed separately, in order.

Notice that the three `echo` commands are prefixed with `@`, which tells Make not to print that command before executing it; considering the purpose of the command is to print some useful information, printing the command and its result would be rather redundant!

Finally, here's what happens when we run `make test` immediately after a `make clean`:

```
gcc -Wall -pedantic -std=c11 -ggdb   -c -o sorter8.o sorter8.c
gcc -Wall -pedantic -std=c11 -ggdb   -c -o bag8.o bag8.c
gcc -Wall -pedantic -std=c11 -ggdb   -c -o readlinep.o readlinep.c
gcc -Wall -pedantic -std=c11 -ggdb sorter8.o bag8.o readlinep.o -lm -o sorter
### 100 lines of input
sed 1,2d ~/cs50-dev/shared/examples/billboard.tsv | cut -f 3 | ./sorter
100 lines:
{Darius Rucker, Future & Lil Uzi Vert, Myke Towers & Juhn, Pooh Shiesty Featuring BIG30, Morgan Wallen, Miranda Lambert, Chris Young + Kane Brown, Dylan Scott, Pop Smoke Featuring A Boogie Wit da Hoodie, Justin Bieber, Keith Urban Duet With P!nk, Black Eyed Peas X Shakira, VEDO, Justin Bieber Featuring Burna Boy, Jordan Davis, Dan + Shay, Justin Bieber Featuring BEAM, Jazmine Sullivan, Rod Wave, Justin Bieber Featuring Dominic Fike, Morray, Sam Hunt, Jake Owen, Morgan Wallen, Justin Bieber, Tiesto, DaBaby, Bad Bunny & Rosalia, Erica Banks, Megan Thee Stallion, All Time Low Featuring Demi Lovato & blackbear, Lil Baby Featuring EST Gee, Justin Bieber, Tenille Arts, Justin Bieber, Eric Church, Justin Bieber, Dustin Lynch, Justin Bieber Featuring The Kid LAROI, Luke Combs, Luke Bryan, Travis Scott & HVME, H.E.R., Glass Animals, Drake Featuring Rick Ross, Moneybagg Yo, Florida Georgia Line, Mooski, Brett Young, Dua Lipa, Taylor Swift, Niko Moon, Ava Max, Luke Combs, Parmalee x Blanco Brown, Megan Thee Stallion Featuring DaBaby, Kali Uchis, Justin Bieber Featuring Khalid, Lil Tjay, Polo G & Fivio Foreign, Chris Stapleton, Maroon 5 Featuring Megan Thee Stallion, Ritt Momney, CJ, Gabby Barrett, The Kid LAROI, Thomas Rhett, SZA, Pop Smoke Featuring Lil Baby & DaBaby, Rod Wave, Machine Gun Kelly X blackbear, Coi Leray Featuring Lil Durk, Giveon, Bad Bunny & Jhay Cortez, Doja Cat, BTS, Lil Baby, Saweetie Featuring Doja Cat, SpotemGottem Featuring Pooh Shiesty Or DaBaby, Tate McRae, Ariana Grande, Justin Bieber, Drake Featuring Lil Baby, Yung Bleu Featuring Drake, Masked Wolf, Justin Bieber, Billie Eilish, Lil Tjay Featuring 6LACK, Pooh Shiesty Featuring Lil Durk, Chris Brown & Young Thug, Ariana Grande, 24kGoldn Featuring iann dior, Pop Smoke, Drake, Dua Lipa Featuring DaBaby, The Weeknd, The Weeknd, Olivia Rodrigo, Silk Sonic (Bruno Mars & Anderson .Paak), Cardi B, Justin Bieber Featuring Daniel Caesar & Giveon, }
### empty input
./sorter < /dev/null
0 lines:
{}
### done
```

In some cases it may be worth writing a test script in bash - especially useful if there are many test cases or looping and conditionals may be helpful.
Then `make test` can run the script, e.g., `bash testing.sh`.

In other cases, especially where the code is a module that has no `main()` of its own, is to write a test program in C - and get the makefile to compile that test program; then `make test` can execute the compiled test program.

## More on macros

Any symbol that is defined in a Makefile like this

    NAME = VALUE

is called a macro.
Once defined, you can expand them using

    ... $(NAME) ...

It is common to define macros for the list of object files and necessary libraries.
Below, our final `Makefile` declares `OBJS` and `LIBS` for this purpose, and uses them in the build command.
Although `sorter` does not need the math library, it's harmless to ask `gcc` to link with the math library and it serves as a good example here.
(Certain libraries, like stdio and stdlib, are linked without you asking.)

```make
OBJS = sorter.o bag.o readlinep.o
LIBS = -lm

# executable depends on object files
sorter: $(OBJS)
	$(CC) $(CFLAGS) $(OBJS) $(LIBS) -o sorter
```

## Complete Makefile

With the addition of the phony targets and a nice header comment, we're done:

```makefile
# Makefile for the "sorter" program that uses the "bag" module.                 
#                                                                               
# David Kotz - April 2017, 2019, 2021                                           

CC = gcc
CFLAGS = -Wall -pedantic -std=c11 -ggdb

OBJS = sorter.o bag.o readlinep.o
LIBS = -lm
TESTINPUT = ~/cs50-dev/shared/examples/billboard.tsv

.PHONY:	all clean test

# default is to build 'sorter'                                                  
all: sorter

# executable depends on object files                                            
sorter: $(OBJS)
	$(CC) $(CFLAGS) $(OBJS) $(LIBS) -o sorter

# object files also depend on include files                                     
sorter.o: bag.h readlinep.h
bag.o: bag.h
readlinep.o: readlinep.h

# two quick test cases
test: sorter
	@echo "### 100 lines of input"
	sed 1,2d $(TESTINPUT) | cut -f 3 | ./sorter
	@echo "### empty input"
	./sorter < /dev/null
	@echo "### done"


clean:
	rm -f sorter
	rm -f *~ *.o
	rm -rf *.dSYM
```

The macro definitions at the top make it easy for a reader to quickly see what program will be built and from what files.

I saved a slight variant of this Makefile as [sorter8-makefile](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter8-makefile) in our examples directory.
You can play with it by copying all the relevant files, e.g.,

```bash
mkdir sorter
cp ~/cs50-dev/shared/examples/{sorter8.c,bag8.[ch],readlinep.[ch]} sorter/
cp ~/cs50-dev/shared/examples/sorter8-makefile sorter/Makefile
cd sorter
make
```
