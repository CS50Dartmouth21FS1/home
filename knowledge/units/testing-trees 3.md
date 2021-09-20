This unit shows a more sophisticated unit-testing approach – *more sophisticated than you will likely need in CS50.*
We include it here for those who may be interested.

This approach makes sophisticated use of the *C preprocessor* to implement glass-box unit testing.

You may have studied the example [tree8](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/trees/tree8/), which demonstrated a binary-tree data structure.
Here we enhance it to become [tree9](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/trees/tree9/) (to a computer scientist, `A` comes next after `9`).

Look at [tree9/tree.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/trees/tree9/tree.c) and its [Makefile](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/trees/tree9/Makefile).

Like the [glass-box unit test for the **bag** module](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/testing-bag.md),
the `tree9` code has a `main()` function, protected by `#ifdef UNIT_TEST ... #endif` so it is only compiled-in when we want to compile a unit test.
This time, however, that unit-testing code block begins with `#include "unittest.h"` to pick up several *preprocessor macros*.
Recall that preprocessor macros are textually replaced by the C preprocessor before the C compiler even runs.
When scanning C code, the preprocessor does *textual substitution* for each defined symbol - it does not evaluate the code at all, it just substitutes the remainder of the line (after the symbol and after a space) into the C program wherever it sees that symbol, optionally inserting the parameter wherever it appears in the macro body.

The **bag** unit test used one simple preprocessor macro; in `unittest.h` we define some far-more sophisticated macros.
Each `#define` defines a fragments; the first two take parameters.

> Because each definition must appear on one "line", I had to use a *line continuation* character (backslash in the last character of the line) to let me format the definitions in a human-readable way.
> I lined up the backslashes so they all look neat.

```c
// each test should start by setting the result count to zero
#define START_TEST_CASE(name) int _failures=0; char* _testname = (name);

// Check a condition; if false, print warning message.
// e.g., EXPECT(dict->start == NULL).
// note: the preprocessor 
//   converts __LINE__ into line number where this macro was used, and
//   converts "#x" into a string constant for arg x.
#define EXPECT(x)                                               \
  if (!(x)) {                                                   \
    _failures++;                                                \
    printf("Fail %s Line %d: [%s]\n", _testname, __LINE__, #x); \
  }

// return the result count at the end of a test
#define END_TEST_CASE                                                   \
  if (_failures == 0) {                                                 \
    printf("PASS test %s\n\n", _testname);                              \
  } else {                                                              \
    printf("FAIL test %s with %d errors\n\n", _testname, _failures);    \
  }

#define TEST_RESULT (_failures)
```

The preprocessor defines a special macro `__LINE__` that is set to the line number of the original source file, as each source line is processed; this is great for printing out the line number where our test case failed.

The preprocessor also has special syntax `#parameter` that substitutes a C string constant for the text of the parameter.
You can see it right at the end of the `EXPECT` macro.
Thus, `EXPECT(tree != NULL)` will produce code that ends with `"tree != NULL");` enabling us to print the line number *and* the condition that failed.
You can't do that with C, only with the preprocessor!

*Warning: I strongly discourage the use of preprocessor macros.*
There are times, however, where they are the right tool for the job, and this is one of those times.

The macros are meant to be used for constructing small unit tests like this one:

```c
/////////////////////////////////////
// create and validate an empty tree
int test_newtree0()
{
  START_TEST_CASE("newtree0");
  tree_t* tree = tree_new();
  EXPECT(tree != NULL);
  EXPECT(tree->root == NULL);

  EXPECT(tree_find(tree, "hello") == NULL);

  tree_delete(tree, NULL);
  EXPECT(mem_net() == 0);

  END_TEST_CASE;
  return TEST_RESULT;
}
```

In the above test, I create a new (empty) tree, try to find something in it, and delete the tree.
Notice, though, that I actually peek *inside* the `struct tree` to verify that all its members are set correctly.

Note, too, how I used those new macros - using `START_TEST_CASE()` to give the test a name and initialize everything, `EXPECT()` to indicate the conditions I expect to be true, `END_TEST_CASE` to print the summary and clean up, and `return TEST_RESULT` to provide a return value for this function.
Here's how that code looks after running it through the preprocessor with `gcc -DUNIT_TEST -E tree.c`:

```c
int test_newtree0()
{
  int _failures=0; char* _testname = ("newtree0");;
  tree_t* tree = tree_new();
  if (!(tree != 
# 244 "tree.c" 3 4
 ((void *)0)
# 244 "tree.c"
 )) { _failures++; printf("Fail %s Line %d: [%s]\n", _testname, 244, "tree != NULL"); };
  if (!(tree->root == 
# 245 "tree.c" 3 4
 ((void *)0)
# 245 "tree.c"
 )) { _failures++; printf("Fail %s Line %d: [%s]\n", _testname, 245, "tree->root == NULL"); };

  if (!(tree_find(tree, "hello") == 
# 247 "tree.c" 3 4
 ((void *)0)
# 247 "tree.c"
 )) { _failures++; printf("Fail %s Line %d: [%s]\n", _testname, 247, "tree_find(tree, \"hello\") == NULL"); };

  tree_delete(tree, 
# 249 "tree.c" 3 4
                   ((void *)0)
# 249 "tree.c"
                       );
  if (!(mem_net() == 0)) { _failures++; printf("Fail %s Line %d: [%s]\n", _testname, 250, "mem_net() == 0"); };

  if (_failures == 0) { printf("PASS test %s\n\n", _testname); } else { printf("FAIL test %s with %d errors\n\n", _testname, _failures); };
  return (_failures);
}
```

If you look closely, you can see the original bits of code (like `tree_delete(tree, NULL)` (with `NULL` expanded!) as well as the expanded `EXPECT` and other macros.

Then the `main()` program runs a series of unit tests, and prints an error if any of them failed:

```c
int
main(const int argc, const char* argv[])
{
  int failed = 0;

  failed += test_newtree0();
  failed += test_newtree1();
  failed += test_treeleft();
  failed += test_treefind();

  if (failed) {
    printf("FAIL %d test cases\n", failed);
    return failed;
  } else {
    printf("PASS all test cases\n");
    return 0;
  }
}
```

Here's what the output looks like when everything passes:

```
$ make unit
gcc -Wall -pedantic -std=c11 -ggdb -DTESTING -DUNIT_TEST tree.c mem.o -o unittest
./unittest
End of tree_delete: 1 malloc, 1 free, 0 free(NULL), 0 net
PASS test newtree0

After tree_insert: 4 malloc, 1 free, 0 free(NULL), 3 net
End of tree_delete: 4 malloc, 4 free, 0 free(NULL), 0 net
PASS test newtree1

After tree_insert: 7 malloc, 4 free, 0 free(NULL), 3 net
After tree_insert: 9 malloc, 4 free, 0 free(NULL), 5 net
After tree_insert: 11 malloc, 4 free, 0 free(NULL), 7 net
After tree_insert: 13 malloc, 4 free, 0 free(NULL), 9 net
End of tree_delete: 13 malloc, 13 free, 0 free(NULL), 0 net
PASS test treeleft

After tree_insert: 16 malloc, 13 free, 0 free(NULL), 3 net
After tree_insert: 18 malloc, 13 free, 0 free(NULL), 5 net
After tree_insert: 20 malloc, 13 free, 0 free(NULL), 7 net
After tree_insert: 22 malloc, 13 free, 0 free(NULL), 9 net
  ann(1)
   bob(2)
    cheri(3)
 dave(4)
End of tree_delete: 22 malloc, 22 free, 0 free(NULL), 0 net
PASS test treefind

PASS all test cases
```

To see what it looks like when a failure occurs, I could either break the tree code (which I'd rather not do!) or break the test code; I'll do the latter by changing one line

```c
  EXPECT(tree_find(tree, "abcd") == &data);
```

to

```c
  EXPECT(tree_find(tree, "abcd") == NULL);
```

and run the test again:

```
$ make unit
gcc -Wall -pedantic -std=c11 -ggdb -DTESTING -DUNIT_TEST tree.c mem.o -o unittest
./unittest
End of tree_delete: 1 malloc, 1 free, 0 free(NULL), 0 net
PASS test newtree0

After tree_insert: 4 malloc, 1 free, 0 free(NULL), 3 net
Fail newtree1 Line 271: [tree_find(tree, "abcd") == NULL]
End of tree_delete: 4 malloc, 4 free, 0 free(NULL), 0 net
FAIL test newtree1 with 1 errors

After tree_insert: 7 malloc, 4 free, 0 free(NULL), 3 net
After tree_insert: 9 malloc, 4 free, 0 free(NULL), 5 net
After tree_insert: 11 malloc, 4 free, 0 free(NULL), 7 net
After tree_insert: 13 malloc, 4 free, 0 free(NULL), 9 net
End of tree_delete: 13 malloc, 13 free, 0 free(NULL), 0 net
PASS test treeleft

After tree_insert: 16 malloc, 13 free, 0 free(NULL), 3 net
After tree_insert: 18 malloc, 13 free, 0 free(NULL), 5 net
After tree_insert: 20 malloc, 13 free, 0 free(NULL), 7 net
After tree_insert: 22 malloc, 13 free, 0 free(NULL), 9 net
  ann(1)
   bob(2)
    cheri(3)
 dave(4)
End of tree_delete: 22 malloc, 22 free, 0 free(NULL), 0 net
PASS test treefind

FAIL 1 test cases
Makefile:29: recipe for target 'unit' failed
make: *** [unit] Error 1
```

Notice how Make exited with error; that's because `unittest` exited with non-zero status: note the code at end of `main()`.
