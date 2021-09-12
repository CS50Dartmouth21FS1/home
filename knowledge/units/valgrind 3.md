In this unit we explore a useful tool, `valgrind`, which can help you find bugs in programs that involve illegal memory access and memory leaks.

We recommend reading this excellent (and brief!) tutorial from Stanford's CS107 class: [Guide to Valgrind](https://web.stanford.edu/class/archive/cs/cs107/cs107.1174/guide_valgrind.html) (some of the notes below are adapted from this guide).

Running a program under `valgrind` results in extensive checking of memory allocations and accesses and, when the program exits, provides a report with detailed information about the context and circumstances of each error.
The output report can be quite verbose and a little difficult to use if you don't know what you are looking for; so let's look at a couple of examples and get a handle on how to read and interpret `valgrind` output.

**The Goal:** A clean report from `valgrind` that indicates "no errors and no leaks."

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=fc862f83-dfe8-4377-8476-ad0d014cf0c2)**

As far as you are concerned when using `valgrind`, there are two general types of feedback you will get regarding your program's usage of memory: memory errors and memory leaks.

## Memory errors

The really obvious and bad memory errors will crash your program outright (e.g., accessing memory that is outside of your program's allocated memory).
The not-so-obvious memory-related errors may "get lucky" most of the time (i.e., touch valid memory), but every once in a while the luck runs out and your program, somewhat mysteriously, fails.
Running `valgrind` on your program can give you insightful information on both of these sorts of errors.

When an error is detected by `valgrind` you should see some output that includes some sort of error description, the offending line of source code, and a little bit of information about the actual memory and what may be going wrong.
You mauy see a few types of memory errors, such as:

* `Invalid read/write of size X`
* `Use of uninitialized value` or `Conditional jump or move depends on unitialized value(s)`
* `Source and destination overlap in memcpy()`
* `Invalid free()`

## Memory "leaks"

When you allocate memory (e.g., `malloc`) but fail to properly `free` that memory when it is no longer needed, this is called a *memory leak*.

As we've seen in class, memory leaks in small, short-lived programs that exit fairly quickly don't cause any noticable issues.
In larger projects that operate on lots of data and/or those that are intended to run for a long time (e.g., web servers), memory leaks can add up quickly and cause your program to become incredibly slow, or fail.

Valgrind allows you to check your programs for memory leaks; to get the best feedback you'll want to specify some additional flags, here for an example program called `prog`:

```bash
$ valgrind --leak-check=full --show-leak-kinds=all ./prog [ARGS]
```

For convenience, we've actually defined a nice myvalgrind alias in the cs50 bashrc file for just this reason.

```bash
$ alias myvalgrind
```

Thus, you can simply run:

```bash
$ myvalgrind ./prog [ARGS]
```

> Note that bash aliases are not available within a Makefile or bash script, so `myvalgrind` will not be recognized in either context.
> There is a similar approach in each case, however.
> See below about [valgrind in scripts](#valgrind-in-scripts)

The easiest way to determine if there is some sort of memory leak is to check the alloc/free counts generated in the `valgrind` output.
Ideally, the counts should match.
If they don't, you'll get a "LEAK SUMMARY" at the end of the report as well as a little bit of information from `valgrind` regarding each of the detected memory leaks (e.g., how many bytes were leaked, where in the code the allocation happened).

When profiling your program, `valgrind` will attempt to categorize any memory leaks into one of four categories:
**(1) definitely lost:** a chunk of memory allocated from the heap but not properly freed, and there is no longer a pointer to the data;
**(2) indirectly lost:** a chunk that was indirectly lost due to "losing" a pointer that provided access to other heap-allocated memory;
**(3) possibly lost:** a chunk not properly freed, but `valgrind` can't determine whether or not there is a pointer to the memory;
**(4) still reachable:** a chunk not properly freed, but the program still retains a pointer to the memory in some way.
Regardless of the category, these are all considered memory leaks and should be fixed!

### Valgrind demo: sorter2.c

Consider again our example program `sorter2.c`.
A trivial change to that program will create a memory leak: comment out the line that free's the memory.

```c
  // print the list of lines, and free as we go
  for (int i = 0; i < n; i++) {
    printf("%d: %s\n", i, lines[i]);
    // free(lines[i]);
  }
```

Call that `sorter2b.c`.
Here is a snippet of the output when running `valgrind` on `sorter2b`:

```
$ cp sorter2.c sorter2b.c
$ emacs sorter2b.c  ## comment out the 'free' line
$ mygcc sorter2b.c readlinep.c -o sorter
$ valgrind ./sorter < beatles
==18979== Memcheck, a memory error detector
==18979== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==18979== Using Valgrind-3.13.0 and LibVEX; rerun with -h for copyright info
==18979== Command: ./sorter
==18979== 
4 lines
0: George
1: Paul
2: Ringo
3: John
==18979== 
==18979== HEAP SUMMARY:
==18979==     in use at exit: 324 bytes in 4 blocks
==18979==   total heap usage: 7 allocs, 3 frees, 9,621 bytes allocated
==18979== 
==18979== 324 bytes in 4 blocks are still reachable in loss record 1 of 1
==18979==    at 0x4C31B0F: malloc (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==18979==    by 0x1089BE: freadLinep (readlinep.c:25)
==18979==    by 0x10884C: readLinep (readlinep.h:32)
==18979==    by 0x1088E8: main (sorter2b.c:26)
==18979== 
==18979== LEAK SUMMARY:
==18979==    definitely lost: 0 bytes in 0 blocks
==18979==    indirectly lost: 0 bytes in 0 blocks
==18979==      possibly lost: 0 bytes in 0 blocks
==18979==    still reachable: 324 bytes in 4 blocks
==18979==         suppressed: 0 bytes in 0 blocks
==18979== 
==18979== For counts of detected and suppressed errors, rerun with: -v
==18979== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 0 from 0)
$
```

Valgrind reports 4 blocks lost - those are the four names we forgot to `free`.
They are still *reachable* because their pointers are still in the `lines[]` array.
For a clean run of valgrind, as expected in all your CS50 labs, the program would need to actually free every block of memory it allocates.


### Valgrind demo: sorter7.c

Let's now look at a more complex program, `sorter7`.
Here is the output when running `valgrind` on `sorter7`:

```
$ myvalgrind ./sorter < beatles 
==27528== Memcheck, a memory error detector
==27528== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==27528== Using Valgrind-3.13.0 and LibVEX; rerun with -h for copyright info
==27528== Command: ./sorter
==27528== 
4 lines:
George
John
Paul
Ringo
==27528== 
==27528== HEAP SUMMARY:
==27528==     in use at exit: 0 bytes in 0 blocks
==27528==   total heap usage: 11 allocs, 11 frees, 9,685 bytes allocated
==27528== 
==27528== All heap blocks were freed -- no leaks are possible
==27528== 
==27528== For counts of detected and suppressed errors, rerun with: -v
==27528== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 0 from 0)
```

All clean!

But let's induce a bug by pretending we forgot to free the contents of each node when deleting list nodes:

```diff
  void listnode_delete(struct listnode* nodep)
  {
    if (nodep != NULL) {
-     if (nodep->line != NULL) {
-       free(nodep->line);
-     }
      free(nodep);
    }
  }
```

This time, four blocks are lost (one for each line of input):

```
$ cat beatles |myvalgrind ./sorter
==28234== Memcheck, a memory error detector
==28234== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==28234== Using Valgrind-3.13.0 and LibVEX; rerun with -h for copyright info
==28234== Command: ./sorter
==28234== 
4 lines:
George
John
Paul
Ringo
==28234== 
==28234== HEAP SUMMARY:
==28234==     in use at exit: 324 bytes in 4 blocks
==28234==   total heap usage: 11 allocs, 7 frees, 5,589 bytes allocated
==28234== 
==28234== 324 bytes in 4 blocks are definitely lost in loss record 1 of 1
==28234==    at 0x4C31B0F: malloc (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==28234==    by 0x108B8F: freadLinep (readlinep.c:25)
==28234==    by 0x1088DC: readLinep (readlinep.h:32)
==28234==    by 0x108913: main (sorter7b.c:37)
==28234== 
==28234== LEAK SUMMARY:
==28234==    definitely lost: 324 bytes in 4 blocks
==28234==    indirectly lost: 0 bytes in 0 blocks
==28234==      possibly lost: 0 bytes in 0 blocks
==28234==    still reachable: 0 bytes in 0 blocks
==28234==         suppressed: 0 bytes in 0 blocks
==28234== 
==28234== For counts of detected and suppressed errors, rerun with: -v
==28234== ERROR SUMMARY: 1 errors from 1 contexts (suppressed: 0 from 0)
```

They are not even reachable, they are "definitely lost" – because the pointers were to strings created by `readLinep`, then stored in list nodes... but then when the listnodes were freed, we lost the pointers to the lines.

The output from valgrind can sometimes be extensive; typically, it is best to start with the first error, try to resolve it, then test again; sometimes a single mistake can result in systematic effects that show up as repeated errors in valgrind.

## <a id="valgrind-in-scripts">Valgrind in scripts</a>

As noted above, `myvalgrind` is a bash alias and thus not available within a Makefile or bash script.
There is a similar approach in each case, however.

In a bash script, you can define a bash variable with the same contents as our alias, and then substitute that variable wherever you want to run valgrind:

```bash
myvalgrind='valgrind --leak-check=full --show-leak-kinds=all'
...
$myvalgrind ./program1 args...
$myvalgrind ./program2 args...
```

In a Makefile, you can define a Make macro (variable) with the same contents as our alias, and then substitute that variable wherever you want to run valgrind:

```make
VALGRIND = valgrind --leak-check=full --show-leak-kinds=all
...
memtest: program
	$(VALGRIND) ./program args...
```
