In this lab you'll continue the Tiny Search Engine (TSE) by coding the Querier according to the [Requirements Spec](REQUIREMENTS.md).

You will also write the Design Spec and Implementation Spec.

Grading will focus on [CS50 coding style](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/labs/style.md) - including consistent formatting, selection of identifier names, and use of meaningful comments - in addition to correctness, testing, and documentation.

***Your C code must compile without producing any compiler warnings.***  You will lose points if the compiler produces warnings when using our CS50-standard compiler flags.

***If your submitted code fails to compile, or triggers a segmentation fault,*** you will fail some or all of our correctness tests.
Write defensive code: each function should check its pointer parameters for NULL, and take some appropriate (safe) action.
Write solid unit tests, test drivers, and use regression testing as your development proceeds.

If your submitted version has *known bugs*, that is, cases where it fails your own test cases, *and you describe those cases in your README file*, we will halve the number of points you lose for those cases.
In short, it is far better for you to demonstrate you *know* about the bug than to submit and hope we won't find it.

***Valgrind should report no memory errors or memory leaks, when crawler exits normally.***
You will lose points for memory errors and leaks reported by valgrind on our tests.


## Preparation

1. Start with the same repository you used for Lab 5.
*Before you begin*, make sure you submitted Lab 5 correctly, [as instructed](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/labs/submit.md).
1. Check to ensure your local repo is clean with `make clean` and everything looks correct according to `git status`.
    **Do not proceed** if you have uncommitted changes or unpushed commits.
    Seek help if you need to sort out your repo or GitHub.
1. Ensure you are again working on the `main` branch **and** that the `main` branch is up to date if you made any changes on `submit5` after branching off `main`.
1. Create a new subdirectory `querier`.

## Assignment

:point_right:
Write the third sub-system of the Tiny Search Engine, the *querier*.
Your design and implementation must follow the **Querier Requirements Spec** (aka "the Specs"), and make good use of our abstract data structures.

**In the top directory,**

 1. Uncomment the commands for querier, so `make` and `make clean` work to build (or clean) the libraries, crawler, indexer, and querier.

**In the `querier` subdirectory,**

1. Add a file `DESIGN.md` to provide the Design Spec for querier.
  Your `DESIGN.md` file should not repeat the information provided in the assignment or in the [Requirements Spec](REQUIREMENTS.md); instead, it should describe the abstract data structures and pseudo code for *your* querier.
1. Add a file `IMPLEMENTATION.md` to provide the implementation spec and testing plan for querier.
  Your `IMPLEMENTATION.md` file need not repeat the information provided in the assignment or other specs; instead, it should describe implementation details specific to *your* implementation.
1. Add a file `README.md` to describe any assumptions you made while writing the querier, any ways in which your implementation differs from the Specs, or any ways in which you know your implementation fails to work.
1. Extend the `README.md` file to clearly **indicate how much of the functionality you implemented**, as [described below](#grading).
1. Write a program `querier.c` according to the Specs.
   Your program should make good use of code from `common.a` and `libcs50.a`, created in earlier labs.
1. Write a `Makefile` that will, by default, build the `querier` executable program.
1. Add a `make clean` target that removes files produced by Make or your tests.
1. Add a `make test` target that tests your querier.
1. Write a `testing.sh` bash script that can be run by `make test`.
 This script must include good comments describing your tests.
 For best results, `make test` should run `bash -v testing.sh`.
1. Save the output of your tests with `make test &> testing.out`.
1. Add a `.gitignore` file telling Git to ignore the binary files (like `querier`) and other unnecessary files in this directory.

### Submission

Add/commit all the code and ancillary files required to build and test your solution; at a minimum your **querier** directory should include the following files:
`.gitignore README.md DESIGN.md IMPLEMENTATION.md Makefile querier.c testing.sh testing.out`
and your **common** directory should contain the following files:
`Makefile index.h index.c pagedir.h pagedir.c word.h word.c`

*Do not commit any data files produced by the crawler or indexer, any binary/object files produced by the compiler, backup files, core dumps, etc.*

The graders must be able to build your TSE *from the top-level directory* without compilation errors and test your querier without run-time errors.

See the [Lab submission instructions](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/labs/submit.md).

## Grading

Lab 6 is scored on the basis of 100 points, with Delivery, Documentation, Style, Testing comprising most of the points.

"Functionality" represents 30/100 points.
In recognition that you might find it best to start simple and slowly enhance your solution as you get the simpler version working, you can earn points on a sliding scale as follows:

 * 10 points if your querier prints the set of documents that contain all the words in the query; you may treat operators ('and', 'or') as regular words.
 * 20 points if your querier also supports 'and' and 'or' operators, but without precedence (in mathematical terms, it treats them as *left associative, equal precedence* operators).
 * 25 points if your querier also supports 'and' precedence over 'or'.
 * 30 points if your querier also prints the document set in decreasing order by score, thus meeting the full specs.

Partial credit is available, of course, per the judgement of the grader, but above is the coarse-grain rubric.

**Please indicate in your `querier/README.md` which of the above subsets of functionality you implemented.**

## Hints and tips

There are some examples and design tips in the [unit about querier](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/querier.md), and following units.

Many of the [Lab4 hints](../crawler/README.md) and
[Lab5 hints](../indexer/README.md) are still relevant.

Processing a query and ranking the results are tricky.
We encourage you to start with a simplified case, test it thoroughly, then enhance.
Easier to code, to test, and to debug, and when facing a deadline it's nice to have a less-functional program that works than a full-functional program that doesn't work.
See the section on [Grading](#grading) regarding the points allocated as you achieve higher levels of functionality.

### Hashtable

How big should your hashtable be?
Well, you can know how many words it will store - because the index file has one word per line, and you can count the number of lines in the index file before creating an index data structure and before loading the file into the structure.
Just think about how the hash-table size (in slots) might relate to the number of words it will need to store.

### Parsing queries

We strongly recommend that your code read the entire query (a line of input) into a single string; then verify the string contains only letters and spaces; if so, then *tokenize* the query string.

To tokenize, write a function that takes a string and builds an array of words (tokens), using space (`isspace`) as the delimiter; each word can be normalized (lower-cased) before being added to the array.
See a [unit](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/querier-chop.md) for inspiration (though significant adaptation is needed).

> Note: from painful experience, we specifically recommend you avoid `strtok` and related functions.

Now that all the character-by-character parsing is behind you, and you have an array of words, you can step through the array to print a *clean query*, that is, with no extraneous spaces and all letters in lower case.

You can then step through the array according to the structure defined in the BNF.
Two tips:

 * Validate the basic structure: neither the first or last word may be an operator, and two operators may not be adjacent.
If valid, proceed to next step; otherwise print a suitable error message.
 * Structure your code to follow the structure of the grammar, which has two non-terminals (`query` and `andsequence`): an inner loop over words in the `andsequence`, accumulating an answer (like a running product) as you go, and stopping when you reach `or` or the end of the array; an outer loop over a sequence of `andsequence` separated by `or`; accumulate an answer (like a running total) as you go.

Read the [unit about parsing expressions](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/querier-expressions.md) for more hints about how this might work.


### Combining results

Suppose you have one `counters` object representing the set of documents in which a given word appears, and another `counters` object representing the set of documents in which another word appears; each counter set is really a set of (docID, count) pairs.
How do you combine them?
Recall [unit about iterators](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/iterators.md).

If you are processing `wordA AND wordB`, the set of documents that match *both* words is the *intersection* of the two sets, and the score for each document (per the specs) is the *minimum* of the count for each document.
So you need a way to intersect two `counters`; we recommend iterating over one set and, for each item, checking whether that document exists in the other set; update the first set according to what you find.
You can use `counters_iterate`, `counters_get`, and `counters_set` to do this, without modifying your `counters` module.

If you are processing `wordA OR wordB`, the set of documents that match *either* word is the *union* of the two sets, and the score for each document (per the definition above) is the *sum* of the count for each document.
So you need a way to union two `counters`; we recommend iterating over the second set and, for each item, checking whether that document exists in the first set; update the first set according to what you find.
Again, you can use `counters_iterate`, `counters_get`, and `counters_set` to do this, without modifying your `counters` module.

While processing an `andsequence` you can compute a 'running product', that is, a `counters` object that represents the intersection of everything seen so far in the sequence.

When processing a query, that is, a disjunction of `andsequence` results, you can similarly compute a 'running sum', that is, a `counters` object that represents the union of everything seen so far in the sequence.


### Ranking results

After parsing and interpreting a query, you will likely have a `counters` object representing the score for each satisfying document.
The `counters` module does not have a 'sort' method or a way to iterate over the items in sorted order.
We suggest you use  `counters_iterate()` to identify the max-scoring item, print it out, and then `counters_set()` to set its counter value to zero; this approach is effectively a 'selection sort'.
Recall [unit about iterators](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/iterators.md).

### ctype

You may find the functions `isalpha()` and `isspace()` useful; read their man pages.
To use them, you need to `#include <ctype.h>`.

### Testing your querier

If your indexer never quite worked, never fear.
You do not need a working indexer to write or test your querier.
Try your querier on the output of our crawler and indexer in the shared directory `~/cs50-dev/shared/tse/output/`.

Your querier reads queries from stdin, one per line.
You can test it interactively, but to do thorough and repeated testing you can write a collection of little files, each of which contains one or more queries to your querier, and run commands like `./querier ... < testquery`.
You might write a short bash script to run the querier through several such test files.
That script might even compare the output to known-good output, for regression testing.

Read the [unit about fuzz testing](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/querier-testing.md);
you are welcome to copy into your repo our `~/cs50-dev/shared/tse/fuzzquery.c`.
If it is used in your testing script, you should commit/push it to your repo.

### Turning off the prompt

I found it useful to print a prompt for an interactive user (when stdin is a "tty", aka teletype, aka keyboard), but not print a prompt when the stdin is not a keyboard (it makes output of my test runs look nicer).
I wrapped it in a little function to abstract the details:

```c
#include <unistd.h>  // add this to your list of includes
/* The fileno() function is provided by stdio,
 * but for some reason not declared by stdio.h, so declare here.
 */
int fileno(FILE *stream);

static void
prompt(void)
{
  // print a prompt iff stdin is a tty (terminal)
  if (isatty(fileno(stdin))) {
    printf("Query? ");
  }
}
```
