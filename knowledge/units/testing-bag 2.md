This unit explores a clever approach to *glass-box* testing of a single-file module, specifically, the **bag** module.

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4bc5bac9-b2e2-4093-9395-ad0e01470000)**

> This video reviews the version of the **bag** module and `bagtest.c` code as of 18 April 2021, which was slightly updated from the original starter kit.  See the latest version [here](https://github.com/cs50spring2021/lab3/tree/main/bag).
<!-- Another bug fix to `bagtest.c` was pushed early on 19 April 2021. -->
<!-- @CHANGEME: the prior line is specific to 21S -->

In [Lab3](https://github.com/cs50dartmouth21FS1/home/blob/main/labs/lab3) you are provided an implementation of the **bag** module; an earlier [unit video](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/lab3-bag.md) walks through the module code.
The module includes a simple unit-test program `bagtest.c`; is that program a glass-box or black-box testing program?

In this unit we explore a *glass-box* approach to unit-test **bag**, right inside `bag.c`; it is found in the example called [bag-unit](https://github.com/cs50dartmouth21FS1/examples/blob/main/bag-unit).
Study the code; here's an overview.

Notice we added a `main()` function at the bottom of `bag.c`.
We put it at the *bottom* of the file because it is not intended to be part of the module's functionality, and thus should not be the focus of someone reading the code for the first time.
We put it *inside* the C source file `bag.c` so it has access to all the private (file-local) types, variables, and functions that are only visible inside this file. 
It can call the public **bag** API, of course, but it can also inspect the innards of the data structure.

We hide this code with a *C preprocessor* macro called `UNIT_TEST`:

```c
#ifdef UNIT_TEST
...
int
main()
{
  ...
}
#endif
```

The preprocessor only includes this code in the C source passed to the compiler if the preprocessor symbol `UNIT_TEST` is defined by `#define UNIT_TEST` in the source file, or `-D UNIT_TEST` on the `gcc` commandline. 
The name means nothing special to the preprocessor - it's just a logical, readable name that is meaningful to us as developers.

In this example, we actually need a helper function and a helper macro:

```c
#ifdef UNIT_TEST
#include <stdbool.h>

// file-local global variables
static int unit_tested = 0;     // number of test cases run
static int unit_failed = 0;     // number of test cases failed

// a macro for shorthand calls to expect()
#define EXPECT(cond) { unit_expect((cond), __LINE__); }

// Checks 'condition', increments unit_tested, prints FAIL or PASS
void
unit_expect(bool condition, int linenum)
{
  unit_tested++;
  if (condition) {
    printf("PASS test %03d at line %d\n", unit_tested, linenum);
  } else {
    printf("FAIL test %03d at line %d\n", unit_tested, linenum);
    unit_failed++;
  }
}
// ...
#endif
```

The above demonstrates a preprocessor macro `EXPECT()` that takes a parameter.
When the preprocessor encounters `EXPECT(...something...)` in the code, it textually replaces it with the rest of the line shown in the definition, substituting for that argument wherever it appears.
Thus `EXPECT(fun)` becomes:

```c
{ unit_expect((fun), __LINE__); }
```

The preprocessor then recognizes a special macro, `__LINE__` and substitutes the line number of the source file.
Thus if `EXPECT(fun)` were on line 99 of `bag.c`, it becomes:

```c
{ unit_expect((fun), 99); }
```

This text is exactly what the C compiler will see.

Once the program is compiled and running, the helper function `unit_expect()` will be called wherever `EXPECT()` appears in the code; as you can see above it will evaluate the condition, print PASS or FAIL, increment a counter of test cases, and increment a counter of failed cases.

We use some global variables here to track the number of test cases and failed cases, allowing our unit-test code to be more complicated and involve a collection of functions called from `main()`, if needed, and yet still allow `unit_expect` to access the counters it needs.
We mark these variables as `static` to indicate they should be visible only within this C file, not beyond.

With this macro we can write compact tests like the following:

```c
  bag_t *bag1 = bag_new();
  EXPECT(bag1 != NULL);
  EXPECT(bag1->head == NULL);
```

We also add a rule to the Makefile, so we can compile and run the unit test:

```make
unittest: bag.c memory.o 
	$(CC) $(CFLAGS) -DUNIT_TEST bag.c memory.o -o unittest
	./unittest
```

Notice this rule expresses a dependence on `bag.c` rather than `bag.o`, because we don't want to use the normal `bag.o`: we want to compile `bag.c` itself with `#define UNIT_TEST` as shown on this commandline.
The rule also places the result in a file `unittest`, rather than `bagtest`, because it is a different program. 
Finally, we add `unittest` to the `make clean` rule.

Here's what it looks like when run:

```
$ make unittest
gcc -Wall -pedantic -std=c11 -ggdb    -c -o mem.o mem.c
gcc -Wall -pedantic -std=c11 -ggdb  -DUNIT_TEST bag.c mem.o -o unittest
./unittest
PASS test 001 at line 199
PASS test 002 at line 200
PASS test 003 at line 211
PASS test 004 at line 212
PASS test 005 at line 213
PASS test 006 at line 214
PASS test 007 at line 217
PASS test 008 at line 218
PASS test 009 at line 219
PASS test 010 at line 220
PASS test 011 at line 221
PASS test 012 at line 222
PASS test 013 at line 227
PASS test 014 at line 234
PASS test 015 at line 240
PASS test 016 at line 242
PASS test 017 at line 243
PASS test 018 at line 244
PASS test 019 at line 245
PASS test 020 at line 246
PASSED all of 20 tests
$ 
```

You may wish to use a similar approach for your modules.
