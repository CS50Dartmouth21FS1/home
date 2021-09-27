You've now had experience building C programs that span multiple `.c` source files.
In each case, you use `#include` to *compile* with the `.h` header file that informs the compiler about the types, functions, and globals supported by that module, and later *link* with the `.o` files that contain a compiled version of the corresponding `.c` source file that implements the module.

> Unlike in languages like Python or Java, to 'import' a module in C you must explicitly  include the module's header file ***and*** link with the module's object file.
> If you forget the former you'll get errors from the compiler; if you forget the latter you'll get errors from the linker.

In larger programs it is common to group together many functions and modules into a *library*, to make it easier for application programmers to link with the entire collection at once.
Thus far you have used several standard libraries -- such as `stdio`, `stdlib`, and `math`.
In all three examples, you `#include` the corresponding header file; the first two are so common that `gcc` links them in without you even asking, whereas you must link with the math library by explicitly adding `-lm` (`-l` for a library in the standard C installation, `m` for math).

In this unit, we learn how to *produce our own libraries*, and how to build and use them with Make and Makefiles.
You'll be following this practice in the Tiny Search Engine (TSE, Labs 4-6).

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=75352fce-227e-4519-99b3-ad1400f11022)**

Recall the Makefile we wrote in the [unit about Makefiles](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/makefiles1.md) -- available as [sorter8-makefile](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter8-makefile) in our examples directory.

In this video demonstration, I modify that Makefile to leverage the TSE `libcs50` library.

I adapted [sorter8.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter8.c) for use with a library-provided *bag*, resulting in file `sorter.c` in one directory, and dropped in the `libcs50` directory as a peer.
This program is now available as [sorter9.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter9.c).

I then adapted the Makefile for *sorter* to leverage `libcs50`; that Makefile is available as [sorter9-makefile](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter9-makefile).
Notice several important features:

* definition of macro `L` as a shorthand for the directory name (not the library name) where the library lives.
By defining it as a macro, the location of that library directory sits in exactly one place in `Makefile` and can easily be updated if the structure of the source-code directories were to change.
Make allows us to substitute the value of single-letter macro names without using parens; thus, I can use `$L` to substitute this directory name.

* addition of `-I$L` to the `CFLAGS`, which tells the compiler to look for `include` files in the directory `$L`.
You may have noticed a similar `-I../lib` in the Lab3 `bag/Makefile`.

* the use of `-I` in the Makefile allows the `.c` files to simply write `#include "bag.h"` without having to specify the path; this feature is incredibly valuable because the C source code should avoid encoding the structure of directories.  A later rearrangement of directory names or structures would not necessitate any changes to the source code - only to the Makefiles.

* the existing `LIBS` macro, meaning "libraries", tells the linker to link with the *math library*; `-lm`

* the new `LLIBS` macro, by which I mean "local libraries", used to tell the linker to link with the libcs50 library; I keep this separate from `LIBS` because I want to include this list as dependendencies for the compiled program.

* the default `all` rule, just to be clear

* the updated `sorter` rule, which indicates dependence on `$(LLIBS)` and also provides them to the linker command (the next line).

* the `sorter.o` rule, which uses `$L` to refer to the include files needed by this program.

* the lack of any rule to build the library `libcs50.a`; it is not the job of this Makefile to build that library.

## Producing library archives

So what *is* a "library" file and how do you make one?

A *library file* is simply an *archive of object files.*
An *archive* is a single file that contains a collection of other files; you've probably downloaded `.zip`, `.tgz`, or `.dmg` files before; those are various forms of archive.
Inside those files is some metadata describing the enclosed files, as well as the (often compressed) data of the original files.

In Unix, there is a specific type of archive used for coalescing object `.o` files into a single `.a` file (`a` for archive, `o` for object).
The linker `ld` knows how to read `.a` files.
It links in any `.o` files that contain symbols that are as-yet unresolved when it reaches that point in its argument list of files; when it pulls in a `.o` file it pulls in that whole `.o` file.
That file may, in turn, reference other symbols - if those are in another `.o` file in the same archive, the linker loads them too.
Any symbols still unresolved after the current `.a` file is exhausted better be in another `.o` or `.a` file later in the argument list.
*Thus, the order of arguments to the linker is important.*

Anyway, a peek inside `libcs50/Makefile` shows how to build an archive:

```make
# object files, and the target library
OBJS = bag.o counters.o file.o hashtable.o hash.o mem.o set.o webpage.o
LIB = libcs50.a
...
# Build $(LIB) by archiving object files
$(LIB): $(OBJS)
	ar cr $(LIB) $(OBJS)
```

The `ar` command manipulates an archive `.a` file; specifically, `ar cr` (c)reates the `.a` file (if needed) and (r)eplaces the enclosed `.o` files with those on its command line.
Thus, `libcs50.a` holds all those `.o` files, which we can (t)ype with `ar t`:

```bash
$ ar t libcs50.a 
bag.o
counters.o
file.o
hashtable.o
hash.o
mem.o
set.o
webpage.o
```

Although programmers who use this archive can see what files are included inside it, if curious, *they do not need to know the number or names of the files*; all they need to know is a path to the archive file.
This approach *decouples* the implementation details of the library between the developers of the library (who need to know) and the users of the library (who do not need to know).

## Building it all

To build the whole program, we need to build the library and then the program.
We could do so, from the directory above, with two calls to Make:

```bash
$ make -C libcs50
make: Entering directory '/thayerfs/home/d31379t/cs50-dev/demo/libcs50'
gcc -Wall -pedantic -std=c11 -ggdb    -c -o bag.o bag.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o counters.o counters.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o file.o file.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o hashtable.o hashtable.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o hash.o hash.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o mem.o mem.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o set.o set.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o webpage.o webpage.c
ar cr libcs50.a bag.o counters.o file.o hashtable.o hash.o mem.o set.o webpage.o
make: Leaving directory '/thayerfs/home/d31379t/cs50-dev/demo/libcs50'
$ 
$ make -C sorter
make: Entering directory '/thayerfs/home/d31379t/cs50-dev/demo/sorter'
gcc -Wall -pedantic -std=c11 -ggdb -I../libcs50   -c -o sorter.o sorter.c
gcc -Wall -pedantic -std=c11 -ggdb -I../libcs50 sorter.o ../libcs50/libcs50.a -lm -o sorter
make: Leaving directory '/thayerfs/home/d31379t/cs50-dev/demo/sorter'
$
```

The `-C` flag tells Make to "first cd to the specified directory and look for a Makefile there".
So `make -C libcs50` effectively runs `make` inside the directory `libcs50`.
The default rule of `libcs50/Makefile` is to build `libcs50.a`, and it does.
The default rule of `sorter/Makefile` is to build `sorter`, and it does.

Notice that *all but one* of the `gcc` commands are executed by Make's implicit rule for producing a `.o` file from a `.c` file; the final `gcc` command is actually running the linker and not the C compiler; its arguments include `sorter.o`, `libcs50.a` and the math library `-lm`.
(In this final command most of the CFLAGS are irrelevant, including the `-I` directive - that's only for include files - but we always include `$(CFLAGS)` on the command line for completeness.)

Indeed, we could write a top-level Makefile that runs those calls to Make, as shown in the Lab4 starter kit; here is the core of it:

```make
# Makefile for CS50 Tiny Search Engine
#
# David Kotz - April 2016, 2017, 2021

MAKE = make
.PHONY: all update clean

############## default: make all libs and programs ##########
all: 
	$(MAKE) -C libcs50
	$(MAKE) -C common
	$(MAKE) -C crawler
	$(MAKE) -C indexer
	$(MAKE) -C querier
...
```

Suppose we then needed to work on enhancing the `sorter` application further.
With the `libcs50.a` already built and tested, we focus our attention on the `sorter` subdirectory:

```bash
$ cd sorter
$ emacs sorter.c   # make some changes
$ make
gcc -Wall -pedantic -std=c11 -ggdb -I../libcs50   -c -o sorter.o sorter.c
gcc -Wall -pedantic -std=c11 -ggdb -I../libcs50 sorter.o ../libcs50/libcs50.a -lm -o sorter
$ 
```

Notice that we can quickly rebuild `sorter` by compiling only `sorter.c` and then linking with the library `libcs50.a`.
No need to compile that library every time!

## Special macros

Make defines several special macro names.
Their names are difficult to remember, but they can be extremely powerful.

Consider the "link" step in our Makefiles above:

```make
sorter: $(OBJS) $(LLIBS)
	$(CC) $(CFLAGS) $(OBJS) $(LLIBS) $(LIBS) -o sorter
```

Although a remarkably general approach, it fails when one Makefile needs to build several programs, each with its own set of object-file dependencies.
Our TSE Makefiles actually write the "link" step as follows:

```make
querier: querier.o $(LLIBS)
	$(CC) $(CFLAGS) $^ $(LIBS) -o $@
```

This format uses Make macro `$^`, which refers to "the dependencies listed in this rule", and `$@`, which refers to "the target of this rule".
The above is thus equivalent to writing

```make
querier: querier.o $(LLIBS)
	$(CC) $(CFLAGS) querier.o $(LLIBS) $(LIBS) -o querier
```

but is less prone to error.
Why? in the latter approach, as the program evolves we might add something to the CC commandline but forget to add it to the dependency list, or vice versa.
Instead, the generic form works for every "link" rule in every Makefile in the entire project.
*Write once, use everywhere*.

In a Makefile that builds several programs (like `indexer` and `indextest`) this approach works best, because it's not possible to define one `$(OBJS)` macro for all programs.

## TSE makefiles

Your Tiny Search Engine, once completed, will include a `Makefile` at the top level and one in each subdirectory.

The top-level `Makefile` recursively calls Make on each of the source directories, as noted above.

Here's a complete build of our TSE solution, typing just one command `make` in the top-level directory:

```
$ make
make -C libcs50
make[1]: Entering directory '/thayerfs/home/d31379t/tse/libcs50'
gcc -Wall -pedantic -std=c11 -ggdb    -c -o bag.o bag.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o counters.o counters.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o file.o file.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o hashtable.o hashtable.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o hash.o hash.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o mem.o mem.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o set.o set.c
gcc -Wall -pedantic -std=c11 -ggdb    -c -o webpage.o webpage.c
ar cr libcs50.a bag.o counters.o file.o hashtable.o hash.o mem.o set.o webpage.o
make[1]: Leaving directory '/thayerfs/home/d31379t/tse/libcs50'
make -C common
make[1]: Entering directory '/thayerfs/home/d31379t/tse/common'
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50   -c -o index.o index.c
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50   -c -o pagedir.o pagedir.c
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50   -c -o word.o word.c
ar cr common.a index.o pagedir.o word.o
make[1]: Leaving directory '/thayerfs/home/d31379t/tse/common'
make -C crawler
make[1]: Entering directory '/thayerfs/home/d31379t/tse/crawler'
gcc -Wall -pedantic -std=c11 -ggdb -DAPPTEST  -I../libcs50 -I../common   -c -o crawler.o crawler.c
gcc -Wall -pedantic -std=c11 -ggdb -DAPPTEST  -I../libcs50 -I../common crawler.o ../common/common.a ../libcs50/libcs50.a  -o crawler
make[1]: Leaving directory '/thayerfs/home/d31379t/tse/crawler'
make -C indexer
make[1]: Entering directory '/thayerfs/home/d31379t/tse/indexer'
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50 -I../common   -c -o indexer.o indexer.c
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50 -I../common indexer.o ../common/common.a ../libcs50/libcs50.a  -o indexer
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50 -I../common   -c -o indextest.o indextest.c
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50 -I../common indextest.o ../common/common.a ../libcs50/libcs50.a  -o indextest
make[1]: Leaving directory '/thayerfs/home/d31379t/tse/indexer'
make -C querier
make[1]: Entering directory '/thayerfs/home/d31379t/tse/querier'
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50 -I../common   -c -o querier.o querier.c
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50 -I../common querier.o ../common/common.a ../libcs50/libcs50.a  -o querier
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50 -I../common   -c -o fuzzquery.o fuzzquery.c
gcc -Wall -pedantic -std=c11 -ggdb  -I../libcs50 -I../common fuzzquery.o ../common/common.a ../libcs50/libcs50.a  -o fuzzquery
make[1]: Leaving directory '/thayerfs/home/d31379t/tse/querier'
$ 
```

All of the `gcc -c` commands were generated automatically by Make; only those creating archives `common.a` and `libcs50.a`, and the executable programs (like `crawler`) were written by hand... and even those were using generic/shorthand notation like this:

```
crawler: crawler.o $(LLIBS)
        $(CC) $(CFLAGS) $^ $(LIBS) -o $@
```
