In this example we examine the implementation of a binary tree serving as a "dictionary" data structure.
Although CS50 won't use this module, or any tree-structured data structure, it is included here for those who want another example of a pointer-based data structure.

## Background

You learned about *trees* and *binary trees* in CS10.
Review those lectures, and code examples:

* [lecture 1 PDF](media/cs10/cs10-tree1.pdf)
* [lecture 2 PDF](media/cs10/cs10-tree2.pdf)
* [BinaryTree.java](media/cs10/cs10-BinaryTree.java)
* [BST.java](media/cs10/cs10-BST.java)

## Tree module

The ultimate tree module ([tree9](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree9/)) has the following files:

* `Makefile`, with rules to compile and test the code.
* `tree.h`, which defines the interface to this module.
* `tree.c`, which implements this module.
* `treetest.c`, a program that runs some simple tests on this module.
* `memory.c` and its `.h` file, a set of functions that replace `malloc` and `free` and help to track memory use - for testing and debugging.
* `readlinep.c` and its `.h` file, a function to read a line of input.

Some things you'll notice in this example:

 * a set of functions exported from `tree.c` via `tree.h` to other C files.
* use of the module `readlinep` to read lines
* use of the module `memory` to track the allocation and freeing of heap memory
* The use of `void*` to represent "a pointer to anything" and its use to build a tree of generic things.
* a global (visible) type `struct tree` that represents the tree as a whole
* a typedef `tree_t` that abstracts that struct as a type
* a private (`static`) type `struct treenode` that represents individual nodes of the tree
* a set of private (`static`) helper functions used only within the module
* The use of recursion - notably, through the use of private helper functions that are recursive.
In this example, the recursion elegantly handles the base case, happily passing `NULL` pointers into `tree_find` and `tree_insert`.
* the use of `strcmp` to compare two strings for less than, equal to, or greater than.

## Precursor examples

The ultimate `tree` was originally developed as a series of successively better examples:

***Example: [tree0](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree0/)***

The original binary-tree module stores integer 'data', each with a unique integer 'key'.
A simple test program (which could be improved!) demonstrates how to use `tree_insert` to build a tree; it could have called `tree_new` the first time, but does not have to do so as long as it keeps track of the pointer returned by the first call.
Note the use of recursion in `tree_insert` and `tree_find`, and look for the use of global, stack, and heap storage.

Notice the "phony" Makefile target that allows you to type `make test` -- compiling and running the test program all in one command -- and the phony target `make clean` that will remove the compiled program, intermediate files, and editor backup files.

***Example: [tree1](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree1/)***

This version introduces a `typedef` to make the `struct treenode` type more opaque and yet more readable for users of this module.

***Example: [tree2](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree2/)***

This version changes the tree from storing integers (with integer keys) to storing arbitrary pointers (with integer keys).
This tree is much more versatile because it can store pointers to any type of thing.
The test code only stores strings (which have addresses in static/global storage) but could have stored things created with `malloc`; it's up to the user of the `tree` module to manage the pointers stored in the tree.

***Example: [tree3](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree3/)***

This code demonstrates an important design pattern:

 * Declare an opaque type `tree_t` that represents the overall data structure, and helper functions to create and destroy such structures.
(By 'opaque' I mean that the contents of the structure are not known outside the module.)

 * Declare a (hidden) type `treenode_t` that represents the internal 'nodes' or elements of the data structure.
(By 'hidden' I mean that neither the structure nor the type are known outside the module.)

This two-struct design further hides the structure and behavior of the tree data structure; the external user of this data structure simply gets a handle of type `tree_t*` when it creates a new tree, and passes that handle back in to all of the 'methods' of this data structure.
Unlike the previous version, it doesn't have to know or understand why `tree_insert` might return a pointer different than the one passed in; that design detail is now rightfully hidden.

This design also gives us the possibility of including other information about the tree - information about the tree as a whole, distinct from information about a specific tree node - within the `struct tree`; we don't yet take advantage of that here.

Notice how some functions inside `tree.c` are marked `static`, which indicates they are only visible within the file.
(They are analogous to `private` methods in Java.) These private function prototypes are declared near the top of the file `tree.c`, so they are known to all other functions within this file, but not in `tree.h`, because no other C files will be able to use these 'private' functions anyway.

***Example: [tree4](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree4/)***

Here, we change the tree so it is keyed on words (strings) rather than integers.
That is, we use a `char*` as key instead of an `int` as key, to enable storage of 'things' that are organized by words instead of numbers.

Notice that it uses `malloc` to make a copy of the key string, and `strcmp` for comparing keys.
If the tree node were to be removed, the code would need to `free(node->key)` before `free(node)`.

***Example: [tree5](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree5/)***

Here we extend the `tree_print` function to accept a function pointer representing a 'callback function' to be used when desiring to print the contents of a node.
Think about it: the tree stores 'things' (pointers of type `void*`) indexed by strings (pointers of type `char*`), but the tree code has no idea what those 'things' are or how to print them.
This version of the code allows the module user (another C file) to provide a function that *does* know how to print those things.
That function must have a prototype like

```c
   void myprint(FILE* fp, char* key, void* thing);
```

and must be provided to the tree module as a parameter, as in `tree_print(tree, myprint)`.


***Example: [tree6](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree6/)***

This version adds a `tree_delete()` function, demonstrating how you need to delete the tree - but first delete its contents.

As in the previous example, `tree_delete()` takes as one parameter a pointer to a function that knows how to delete (free) the items within the tree.
(Some items may be quite complex and need a bit of logic to delete; others may not need to do anything, so NULL is acceptable as a parameter here.)

It's important to recognize that calling `tree_delete(t)` may delete all the memory allocated to store the nodes (and keys) in a tree `t`, but does not change the pointer `t` itself.
The variable `t` is a pointer of type `tree_t*`, and its value is an address.
When we call `tree_delete(t)` we pass that value to the local variable within `tree_delete()` representing that parameter.
Even if `tree_delete()` chose to change the value of its parameter, e.g., to set it to NULL, that would have no effect on the caller's variable `t`.

Thus, after the call, `t` still holds an address, a pointer to the old location of the tree; the caller's code must be careful not to use that pointer again!
Stylistically, it may be wise to write

```c
	tree_delete(t, mydelete);
	t = NULL;
```
so any accidental use of `t` will at least trigger a segfault.
This approach is not required - just a defensive programming measure - one that you should consider adopting.

***Example: [tree7](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree7/)***

This version shows how to add "testing" code to your module with `#ifdef`, and how to turn that on and off from the Makefile; and how you might track malloc/free in a module to detect memory leaks or ensure that everything gets free'd.
Review the `mem` module in
[mem.h](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree7/mem.h) and
[mem.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree7/mem.c).

***Example: [tree8](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree8/)***

tree8 finally has an interesting test program!
It reads weather reports from the National Weather Service and stores them in the binary tree.
This example shows how you might use the tree to store something non-trivial (not a string, not an integer).
It shows how you might track malloc/free in the main program and notice a memory leak.
It shows a more interesting 'print' function, and a simple 'delete' function.
Try it!

***Example: [tree9](https://github.com/CS50Dartmouth21FS1/examples/blob/main/trees/tree9/)***

This example integrates a unit test using a fancy set of pre-processor macros for unit testing; both of these concepts (unit tests and pre-processor) are subject of later units.

***diffs***

To better see the evolution of code, for example, the differences between version 8 and version 9:

```bash
cd cs50-dev/shared/examples/trees
diff tree[89] | less
```

It's worth noting how little code we needed to add to support `tree_delete()` in tree6, to track malloc/free in tree7, and no changes to tree.c to support a tree of weather forecasts vs a tree of strings in tree8.
These diff files are a hint about where you should look for changes in each new version.
