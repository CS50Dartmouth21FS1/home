To truly understand C, and in particular, the use of pointers, you need to understand how C programs use memory.
These same concepts underly nearly all procedural languages, though some languages cover up the details.

**[:arrow_forward: Video lecture](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1b3ddb77-7569-4705-9e20-ad09018889a7)**

In this unit we use a set of [slides](media/memory/memory-pointers-C.pdf) to explain memory, addresses, and pointers, complementing the notes below.
The slides reference several examples:

* [pointer0.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/pointer0.c)
* [pointer1.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/pointer1.c)
* [pointer2.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/pointer2.c)
* [pointer3.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/pointer3.c)
* [crash.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/crash.c)
* [sorter2.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter2.c)

Since then, we added another example:

* [pointers.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/pointers.c)

## Memory, and addresses

Every bit of information the computer needs must be stored somewhere in memory - whether instructions or data.
The computer's memory is a sequence of bytes, each byte with its own numeric *address*.
If the computer has one megabyte of memory, these 2^20 bytes will be numbered from 0 to 2^20-1, that is, from 0 to 1,048,575.
Or, since we're computer scientists, we write numbers in [hexadecimal](https://en.wikipedia.org/wiki/Hexadecimal) rather than decimal; the bytes are numbered from 00000 to FFFFF.
In practice, we tend to write hexadecimal numbers in C notation: 0x00000 to 0xFFFFF.

Given a variable, we can use a C operator `&` to get its address.
You might read this operator as "address of...".

```c
int x = 42;    // x is an integer with value 42
int* xp = &x;  // xp is a pointer to x
int y = *xp;   // y is a copy of the integer (42) pointed at by xp
```

The first variable `x` is an integer.
The second variable `xp` is a pointer to an integer, and it is initialized to the address in memory where `x` is stored.
The third variable is also an integer, and it is initialized to the contents of memory at the address given by the pointer called `xp`; in the expression `*xp` we say the code is "dereferencing the pointer `xp`".
Because `xp` has type `int*`, `*xp` has type int, and we are able to assign that value to the new variable `y`.

> By the way, the `NULL` pointer is simply address zero (0x00000).
The OS arranges for that memory segment to be illegal for reading or for writing, so if you try to dereference a null pointer for reading or writing (think `char* p=NULL; char c = *p;`) the OS will catch it and trigger a 'segmentation fault'.
> Compile and run [crash.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/crash.c) to experience this 'segfault' for yourself!

Check out this [animated explanation of pointers](https://www.youtube.com/watch?v=5VnDaHBi8dM).
Fun!

### Four regions of memory

When you run your program, it becomes a *process* within Unix.
(To see a list of all your processes, type `ps` at the bash commandline.
To see *everyone's* processes running on the system, try `ps ax`.)
The OS gives each process its own memory, completely independent of all other processes.
Each byte in memory has a unique numeric *address*, such as from 0x00000000 through 0xFFFFFFFF.

C stores all your program's code, and all of the data your process manipulates, in four distinct regions of memory (aka *segments*).
There's nothing special about each region - they're all somewhere in that linear sequence of bytes - but C manages each differently.
The [pointers.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/pointers.c) example does a nice job of explaining and demonstrating the fact that pointers are just addresses, and distinguishing these four regions.

Let's look at each region in turn.

#### Code memory (aka text)

Your program's executable code (including any libraries like stdio or math) is copied into memory so the processor can read the instructions and execute them.
C stores all that code in a segment called the *code* segment (duh).
When your process is created, the C runtime system expects to call a function called `main()`, and your code takes it from there.
Because all your code is in memory, every function has an *address* in memory, specifically, in the code segment.

> Sometimes the code segment is called the *text* segment.

#### Global memory (aka static)

The simplest region is called 'global' (or 'static') storage, and it's where global variables live.
If you define a constant or variable outside of any function, it has

 * *global scope* - meaning that it is visible to all code in any function within the current file (well, from the point it is declared down to the end of the file), and
 * *static storage* - meaning that C allocates space for it (and initializes the contents) before the program begins executing; its address will never change throughout the execution of the program (hence the name 'static').

For example,

```c
const float pi=3.1415926535;
const char usage[] = "Usage: dog [-n] [-m mapfile] [-] [filename]...\n";
int error_count = 0;

int main()
{
...
}
```

The above declares three global variables, two of which are constants and one of which is a variable.
All are visible to functions defined below their declaration.
By convention, global variables are always declared at the top of a C file, after `#include` directives and before any function definitions.

Global variables can be handy, and sometimes necessary, but it is good style to avoid use of global variables.
(Global constants are generally ok.)
Well-modularized programs "keep their data close" - passing the data (or pointers to data) among functions.

There are two kinds of 'globals' in C: globals within a C file, and globals across all C files in the program.
The former are reasonable, if used carefully, but the latter are more dangerous; avoid cross-file global variables at all costs.

#### The Stack (for local variables)

Another segment of memory is the *stack*, which holds local variables and tracks the calls and returns of functions as the program executes.
We call it a stack because it acts much like the 'stack' data-structure you saw in CS10; calling a function pushes data onto the stack, and returning from that function pops data off the stack.

All of the example code we've seen so far makes extensive use of local variables; these variables are defined within a function and have

* *local scope* - meaning that the variable is visible within the function only, and
*  *stack storage* - meaning that C allocates space for the variable within the stack frame for this particular instance of this function call.

**Note:** Local variables include the function's parameters.

When the program starts, C allocates a chunk of bytes on the stack to hold all the variables defined in `main`.
This chunk is called a 'stack frame'.
*It does not initialize these bytes* - the program must initialize variables, either in the variable definition or in the code to follow.
Later, when `main` calls another function, say, `freadLine`, it allocates another chunk of bytes on the stack (we say it *pushes another frame on the stack*) to hold the variables defined by `freadLine`.
When `freadLine` calls `fgetc`, it pushes another stack frame on the stack, a chunk of bytes to hold the variables defined within `fgetc`.
When `fgetc` returns, it *pops the frame off the stack*.
When `freadLine` returns, it pops that frame off the stack.
The local variables defined in `freadLine` are not just syntactically inaccessible to `main` (out of scope), their memory is no longer allocated.
Indeed, when `main` calls another function, say, `printf`, a stack frame is pushed on to the stack for `printf`, re-using the same memory that had been allocated to `freadLine ` and `fgetc`.

#### The Heap (dynamically allocated memory)

The final region of data memory is called the *heap*.
It is a large region of memory managed by a pair of library functions called `malloc()` and `free()`.
Each call to `malloc()` selects a chunk of bytes from the heap region, and returns a pointer to that chunk.
It keeps careful records of which chunks have been allocated, and which are free.
It is the programmer's responsibility to, eventually, return unused chunks by calling `free(p)` where `p` is a pointer earlier returned by `malloc`.
If the programmer forgets to call `free`, that chunk can never be reused, and eventually `malloc` will run out of free chunks to be allocated.
(This situation is called a 'memory leak.')   It is also the programmer's responsibility not to call `free` multiple times for the same pointer; doing so may corrupt the records kept by the memory-allocation library, and will likely lead to program crashes.
We'll get into the details in one of the next units.

### On exit

 When the process exits, all its memory is released - the four segments (code, global, stack, and heap) disappear.

 That said, it is good style to explicitly `free` all the memory you have allocated with `malloc`.
 In CS50, this practice is *required*.

## crash.c

Let's look at [crash.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/crash.c) to see what happens when we dereference a NULL pointer.

```c
int main()
{
  int *x = NULL;

  *x = 42;

  return 0;
}
```

when we run it, we get the dreaded ***segmentation fault***, so-called because the program is violating the 'segmentation' of memory, in this case, trying to access a 'segment' that is marked unreadable.

```
[~/cs50-dev/demo]$ ./crash
Segmentation fault (core dumped)
[~/cs50-dev/demo]$
```

The result is a file called `core`, which is copy of the process's memory at the moment the program crashed.
Let's examine the core file using the `gdb` debugger.

> If you run this program and don't see a file `./core`, run the `ulimit` command first.

```
$ ulimit -c unlimited
$ ./crash 
Segmentation fault (core dumped)
$ file core
core: ELF 64-bit LSB core file x86-64, version 1 (SYSV), SVR4-style, from './crash', real uid: 23925, effective uid: 23925, real gid: 168108, effective gid: 168108, execfn: './crash', platform: 'x86_64'
$ gdb crash core
GNU gdb (Ubuntu 8.1.1-0ubuntu1) 8.1.1
...
Reading symbols from crash...done.
[New LWP 31323]
Core was generated by `./crash'.
Program terminated with signal SIGSEGV, Segmentation fault.
#0  0x000055a2b123060a in main () at crash.c:14
14	  *x = 42;
(gdb) 
```

We can see exactly which line was being executed when the fault occurred, and we  can explore all of the variables in place at the time.
We'll explore `gdb` in more depth in another unit.
