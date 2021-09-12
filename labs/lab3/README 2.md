# Lab 3 - Data structures in C

In this lab you will develop some general-purpose data structures that, with modular design, can be re-used for other labs - most notably, the Tiny Search Engine (Labs 4-5-6).

Grading will focus on [CS50 coding style](https://github.com/cs50dartmouth21FS1/home/blob/main/labs/style.md) - including consistent formatting, selection of identifier names, and use of meaningful comments - in addition to correctness and testing.

***Your C code must compile without producing any compiler warnings.***
You will lose points if the compiler produces warnings when using our CS50-standard compiler flags (as shown in the `bag/Makefile`).
Recall our policy about [programs that crash](https://github.com/cs50dartmouth21FS1/home/blob/main/logistics#crash).

***If your submitted code fails to compile, or triggers a segmentation fault,*** you will fail all/some of our correctness tests, and lose points for correctness on those test cases.
Write defensive code: each function should check its pointer parameters for NULL, and take some appropriate (safe) action.
Write solid unit tests, test drivers, and use regression testing as your development proceeds.

If your submitted version has *known bugs*, that is, cases where it fails your own test cases, *and you describe those cases in your README file*, we will halve the number of points you lose for those cases.
In short, it is far better for you to demonstrate you *know* about the bug than to submit and hope we won't find it.

## Assignment

:point_right:
[Accept the assignment](https://classroom.github.com/a/CvID-dRJ), which will lead you to clone a starter kit that implements the **bag**, **file**, and **mem** modules.

:point_right:
Add three new modules, each of which defines a different data structure.

<!-- @CHANGEME: adjust point balance to match rubric -->

* (25 points) **set**
* (25 points) **counters**
* (40 points) **hashtable**
* (10 points) overall, including git behavior

### About the data structures

The specific data structures are defined in the sections below.

In the table below, we compare these data structures with the two we explored in class.
Most of these data structures allow you to store a collection of "items".
Both the **set** and **hashtable** are examples of an abstract data structure called a *dictionary*, which provide methods like `insert(key, item)` and `item = retrieve(key)`, where the `key` allows the structure to distinguish among the items it stores.


| Behavior                | **list**   | **bag**  | **set**| **counters**    | **hashtable** |
|:----------------------- |:---------- |:-------- |:------ |:--------------- |:------------- |
| stores an *item*        | yes        | yes      | yes    | no              | yes           |
| uses a *key*            | no         | no       | yes    | yes             | yes           |
| keeps items in order    | yes        | no       | no     | no              | no            |
| retrieval               | first item | any item | by key | by key          | by key        |
| insertion of duplicates | allowed    | allowed  | error  | increment count | error         |
  

Notice that

* a **list** keeps _items_ in order, but a `bag` or a `set` does not.
* a **set** and **hashtable** allow you to retrieve a specific _item_ (indicated by its *key*) whereas a **bag** might return any _item_.
* because the **bag** and **list** don't distinguish among _items_ they store, they can hold duplicates; the others cannot.
* the **counters** data structure maintains a set of counters, each identified by a _key_, but it stores no _items_.
Instead, it keeps a counter for each key. Attempting to insert a duplicate _key_ results in an increment of the counter.

### bag

A **bag** is an unordered collection of _items_.
The bag starts empty, grows as the caller adds one _item_ at a time, and shrinks as the caller extracts one _item_ at a time.
It could be empty, or could contain hundreds of _items_.
_Items_ are indistinguishable, so the _extract_ function is free to return any _item_ from the bag.

The starter kit includes our **bag** module, in which `bag.c` implements a bag of `void*`, and exports the following functions through `bag.h` (see that file for more detailed documentation comments):

```c
bag_t* bag_new(void);
void bag_insert(bag_t* bag, void* item);
void* bag_extract(bag_t* bag);
void bag_print(bag_t* bag, FILE* fp, void (*itemprint)(FILE* fp, void* item));
void bag_iterate(bag_t* bag, void* arg, void (*itemfunc)(void* arg, void* item) );
void bag_delete(bag_t* bag, void (*itemdelete)(void* item) );
```

### set

A **set** maintains an unordered collection of _(key,item)_ pairs; any given _key_ can only occur in the set once.
It starts out empty and grows as the caller inserts new _(key,item)_ pairs.
The caller can retrieve _items_ by asking for their _key_, but cannot remove or update pairs.
Items are distinguished by their _key_.

Your `set.c` should implement a set of `void*` items with `char*` _keys_, and export exactly the following functions through `set.h` (see that file for more detailed documentation comments):

```c
set_t* set_new(void);
bool set_insert(set_t* set, const char* key, void* item);
void* set_find(set_t* set, const char* key);
void set_print(set_t* set, FILE* fp, void (*itemprint)(FILE* fp, const char* key, void* item) );
void set_iterate(set_t* set, void* arg, void (*itemfunc)(void* arg, const char* key, void* item) );
void set_delete(set_t* set, void (*itemdelete)(void* item) );
```

### counters

A **counter set** is a set of counters, each distinguished by an integer _key_.
It's a set - each _key_ can only occur once in the set - and it tracks a *counter* for each _key_.
It starts empty.
Each time `counters_add` is called on a given _key_, the corresponding *counter* is incremented.
The current *counter* value can be retrieved by asking for the relevant _key_.

Your `counters.c` should implement a set of integer counters with `int` _keys_ (where _keys_ must be non-negative) and export exactly the following functions through `counters.h` (see that file for more detailed documentation comments):

```c
counters_t* counters_new(void);
int counters_add(counters_t* ctrs, const int key);
int counters_get(counters_t* ctrs, const int key);
bool counters_set(counters_t* ctrs, const int key, const int count);
void counters_print(counters_t* ctrs, FILE* fp);
void counters_iterate(counters_t* ctrs, void* arg, void (*itemfunc)(void* arg, const int key, const int count));
void counters_delete(counters_t* ctrs);
```

### hashtable

A **hashtable** is a set of _(key,item)_ pairs.
It acts just like a *set*, but is far more efficient for large collections.

Your `hashtable.c` should implement a set of `void*` with `char*` _keys_, and export exactly the following functions through `hashtable.h` (see that file for more detailed documentation comments):

```c
hashtable_t* hashtable_new(const int num_slots);
bool hashtable_insert(hashtable_t* ht, const char* key, void* item);
void* hashtable_find(hashtable_t* ht, const char* key);
void hashtable_print(hashtable_t* ht, FILE* fp, void (*itemprint)(FILE* fp, const char* key, void* item));
void hashtable_iterate(hashtable_t* ht, void* arg, void (*itemfunc)(void* arg, const char* key, void* item) );
void hashtable_delete(hashtable_t* ht, void (*itemdelete)(void* item) );
```

The starter kit provides code for the hash function.		

### General notes

* Your modules must implement *exactly* the above interface.
Do not modify those function prototypes.
Indeed, you should have no need to edit the header (`.h`) files we provide.
* If you need support data types (likely `struct` types), those should be defined within your module's source file (`set.c`, etc.) so they are not visible to users of the module.
* If your module needs helper functions, those should be defined within that module's source file and marked `static` so they are not visible to users of the module.
* Your modules must print *nothing* (except, of course, in the `xxx_print()` function).
If you want to add debugging prints, they must be protected by something like `#ifdef DEBUG` or `#ifdef TEST`.
(You can see some examples in `bag.c` where we've protected some debugging code with `#ifdef MEMTEST`, and a spot in the `bag/Makefile` that controls that flag from the compiler command line.)
* Your modules must have no global variables.
* Your modules must have no `main()` function; as modules, they are meant to be used by other programs (exception: unit-test code hidden by `#ifdef`).
* Your modules **set** and **hashtable**, like **bag**, store `void*` items; this type is C's way of describing a "pointer to anything".
	* The caller (user of your module) must pass a pointer (address of some item) to your code; your data structure holds that pointer, and later returns it to the caller in response to an 'extract' or 'find' operation.
	* Your module doesn't know, or doesn't care, what kind of things the items are. Your module doesn't allocate memory for items, free memory for items, or copy items - it just tracks the *pointer* to the item.
	* The caller is responsible for the *item* pointer, which must be allocated (somehow) by the caller.
The modules' `_delete` function (like a destructor) allows the caller to provide a custom `itemdelete` function that knows how to free any memory consumed by an item.
	* For this reason, the caller must avoid inserting the same item (same address) multiple times; later, the `itemdelete` method would be called multiple times on that item... which could lead to trouble.
* Both **set** and **hashtable** work with string-type keys.
When adding a new item with `set_insert()` or `hashtable_insert()`, both modules make their own copy of the string - presumably in memory allocated by `malloc()`.
	* The module is then responsible for this memory - and later freeing it - just like any other memory it allocates.  This 'copy' semantic is convenient for the caller, who need not worry about how to allocate and manage the key string after inserting it into the set or hashtable.
	* You may assume that a non-NULL `key` is a proper C string; that is, it is null-terminated.
* Your code must have no memory leaks. We will check!
	* You may find the [**mem** module](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/lab3-mem.md) (provided) useful - or use the native `malloc` and `free`.
	* You may find [valgrind](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/valgrind.md) useful.
* Your module must have a unit-test mechanism, either included within the module code (see, for example, the bottom of `file.c` in the [file module](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/lab3-file.md)) or as a test driver (see, for example, `bagtest.c` in the [bag module](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/lab3-bag.md)).
* Your modules must each have a `Makefile` to compile and test the module code.
	* Your `Makefile` must have a `make test` target that runs your unit test.
	* Your `Makefile` should have a `make clean` target that cleans up the directory of any files created by `make` or `make test`.
* Your code should, as always, follow [CS50 coding style](https://github.com/cs50dartmouth21FS1/home/blob/main/labs/style.md).
  Notice that the module interfaces, defined in the `.h` files we provide, follows that naming convention, preceding each module's function names with the name of the module, and an underscore (e.g., `counters_new()`).

## Hints

You are encouraged to follow the style and layout of the `bag` module when developing new modules.

You can also learn a lot from our [binary trees](https://github.com/cs50dartmouth21FS1/examples/blob/main/trees) example.
You are welcome to copy snippets of code from this (or any other) CS50 example code as long as you add a comment indicating you've done so.

We suggest implementing the **set** and **counters** as simplified linked lists, much like we did for `bag`.
Each should be an independent implementation because they differ in detail and semantics.

Your **hashtable** module, on the other hand, should make use of the **set** data structure.
Indeed, your hashtable should likely be an array of pointers to sets.
Allocating an array of pointers can be tricky; recall the unit about [C arrays](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/c-arrays.md).

***Linked lists*** were demonstrated in [sorter4.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/sorter4.c) through [sorter7.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/sorter7.c), although you will need to generalize.
They were also covered in CS10; see the [notes](https://www.cs.dartmouth.edu/~tjp/cs10/notes6.html).

***Hashtables were also covered in CS10:***
[notes](https://www.cs.dartmouth.edu/~tjp/cs10/notes11.html),
[slides](cs10-hashtables.pdf).

## What to hand in

*Each* of the four subdirectories of your `lab3` repo must include the following files:

1. `README.md`,
	which describes how your program should be compiled and executed, along with an explanation of your approach to the implementation and any assumptions you made about the assignment.
	See `bag/README.md` for an example.

1. `Makefile`, with the default target building the relevant `.o` file, and two additional "phony" targets: `make clean` that cleans the directory of anything built by Make; and `make test` that will compile and run a test program with appropriate parameters and inputs.

2.  `testing.out`,
	which is the output of running `make test &> testing.out` inside that subdirectory.
	Your Makefile may implement the tests itself or run a program or script to handle all the testing.
	You should include all necessary test materials (C programs, bash scripts, input files) in your submission.
	Your Makefile and/or test program should include comments explaining your test.
	As an example, see the `bag` module in the starter kit; that program rolls all the tests into one C program.

3. `.gitignore`,
	which should list any files you do not want git to commit, ever.
	Notably, this should include the name of any executable binary programs your Makefile produces, and scratch files produced by your testing programs.

Your `lab3` directory must contain the following, plus any programs, scripts, or data files you need for running your tests:

```
.
|-- .gitignore			# provided by starter kit
|-- Makefile			# provided by starter kit
|-- README.md			# be sure to include your name and username
|-- bag
|   |-- .gitignore		# provided by starter kit
|   |-- Makefile		# provided by starter kit
|   |-- README.md		# provided by starter kit
|   |-- bag.c			# provided by starter kit
|   |-- bag.h			# provided by starter kit
|   |-- bagtest.c		# provided by starter kit
|   |-- test.names		# provided by starter kit
|   `-- testing.out		# provided by starter kit
|-- counters
|   |-- .gitignore
|   |-- Makefile
|   |-- README.md
|   |-- counters.c
|   `-- counters.h		# provided by starter kit
|-- hashtable
|   |-- .gitignore
|   |-- Makefile
|   |-- README.md
|   |-- hash.c			# provided by starter kit
|   |-- hash.h			# provided by starter kit
|   |-- hashtable.c
|   `-- hashtable.h		# provided by starter kit
|-- lib
|   |-- Makefile		# provided by starter kit
|   |-- README.md		# provided by starter kit
|   |-- file.c			# provided by starter kit
|   |-- file.h			# provided by starter kit
|   |-- mem.c			# provided by starter kit
|   `-- mem.h			# provided by starter kit
`-- set
    |-- .gitignore
    |-- Makefile
    |-- README.md
    |-- set.c
    `-- set.h			# provided by starter kit
```

> This listing is produced by the `tree` program.  Neat, huh?

## Submitting your lab

Please read those [instructions](https://github.com/cs50dartmouth21FS1/home/blob/main/labs/submit.md) carefully!
