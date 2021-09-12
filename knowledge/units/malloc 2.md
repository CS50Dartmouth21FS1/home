In this unit we explore `malloc()` and `free()`, which allocate and de-allocate memory from the heap.
We provide two examples; one is an array of pointers to strings, and the other is a linked list.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d06c3356-4852-4c4f-87cd-ad0a000e0eb7)**

## Dynamic memory allocation

C does not have language support for dynamically allocating new 'things'.
Instead, the programmer has to call a library function called `malloc()` to allocate a new chunk of memory from the *heap segment*, and later call `free()` to return that chunk of memory to the heap.
The programmer has to remember to initialize the chunk of bytes received from `malloc()` -- which otherwise should be assumed to contain random data.
The programmer has to be careful allocate a large enough chunk to hold the data she intends to store there, and not to use pointers to write "outside" that chunk of memory.
Lots of flexibility and power - but as with any great power, one must take great care in using it.

> In Java, you can use `new` to dynamically create a new object, and `delete` to discard an object created with `new`, but for the most part the Java compiler and runtime handles object deletion and memory recovery automatically - it's called 'garbage collection.'

<!-- 
**[Slides with detailed examples](media/malloc/MemoryFunctions.pdf)**
-->

There are two functions you must understand:

* **malloc:** `p = malloc(n)` allocates a chunk of `n` bytes of heap memory; the memory contents remain uninitialized.
* **free:** `free(p)`, where `p` is a pointer to a chunk previously returned by `malloc` or one of its relatives below, releases that portion of heap memory for future use.

The function `malloc()` returns a pointer of type `void*`, that is, a *pointer to unspecified type*.
That's because `malloc` simply finds space in memory for `n` bytes, and returns the address of the first byte of that chunk of memory.
It's up to you to assign that pointer to a variable of a specific type, such as `char*` for a string, or `int*` for an array of integers.
It is also up to you to initialize the contents of that memory: `malloc` makes no guarantees about the contents, which you should assume are garbage.

There are two related functions that can be worth knowing:

* **calloc:** `p = calloc(count, size)` allocates `count*size` bytes of heap memory *and* initializes the contents to zero; this call is appropriate when you want to allocate an array of `count` items, each of `size` bytes.
* **realloc:** `p = realloc(p, n)`, where `p` is a pointer previously returned by `malloc` or `realloc`, expands (or shrinks) its allocation to `n` bytes.

For an example using `realloc`, read about [how readLinep() works](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/readlinep.md).

Like `malloc()`, both of these return a pointer of type `void*`, that is, a *pointer to unspecified type*.
Unlike `malloc()`, `calloc()` initializes the chunk to all-zeroes, and `realloc()` copies the original chunk to the new chunk, if needed; if the new chunk is larger than the old chunk, it does not initialize the space beyond the original chunk.

## Example: sorter2

Let's revise our [sorter1.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/sorter1.c) program, using an array of pointers instead of a two-dimensional array of characters: the result is [sorter2.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/sorter2.c).
It uses a new "readLine" function called `freadLinep`, which allocates space for the string returned.

> As an aside, notice `readlinep.c` has a "unit test" at the bottom.
To try it, compile with a flag that defines `UNIT_TEST` as follows:
> `mygcc -D UNIT_TEST readlinep.c -o readlinetest`

Here, the two-dimensional `lines` array is replaced by a one-dimensional array of string pointers, which is more common in C, and which allows the names to be of any length - each array entry points to a dynamically allocated memory from the heap, big enough to hold that name.

You may want to read more about the [implementation](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/readlinep.md) of `readLinep()`, which is a good example of character-by-character input, string construction, and use of `realloc` to grow an array.

## Example: sorter3

We can extend [sorter2.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/sorter2.c) to sort the array using the library function `qsort()`, which implements the quicksort algorithm, resulting in [sorter3.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/sorter3.c).

This example is admittedly a bit advanced for us at this point; it demonstrates use of the generic pointer type `void*`, the need to *cast* our pointers to that type, and the use of function pointers.
We'll come back to function pointers later!


## Memory leaks and stray pointers

***For every `malloc` there must be exactly one matching `free`.***

* If you allocate space but never free it, you've created a *memory leak*; as the program runs, the process memory size will grow and grow, and eventually run out of memory.
* If you free a pointer you've never allocated – or free the same pointer twice – you may cause the heap manager to corrupt its data structures and trigger a segmentation fault at some future time – often a seemingly random time.

**Tip:** whenever you write code invoking `malloc`, immediately write code to make the corresponding `free` call; or, document whose responsibility is to `free` that pointer.
That way, you won't forget to write that code later.

**Tip:** Whenever you call `free(p)`, it's good practice to immediately set `p=NULL`.  
Not because you have to – it makes no difference to the heap, and has no actual effect on the allocation of memory – but because that step will prevent you from accidentally re-using that now-defunct pointer `p`, a.k.a. "stray pointer".
If you do, you'll get a clean segmentation fault instead of unpredictable behavior.

> When the process exits, all its memory is free - the four segments (code, global, stack, and heap) disappear.
> Nonetheless, in CS50 we expect you to clean up your mess before you exit - there should be no un-free'd memory remaining!

An upcoming unit presents the [valgrind](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/valgrind.md) tool, which helps track memory usage and identify various kinds of memory errors.
