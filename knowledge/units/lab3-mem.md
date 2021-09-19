This unit is a quick video walk-through of **mem module** in the [Lab 3](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/labs/lab3) starter kit, and its Makefile.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=75860588-a1c3-4a4c-8a1d-ad0d01176ac4)**

The module implements these handy functions:

```c
void* mem_assert(void* p, const char* message);
void* mem_malloc_assert(const size_t size, const char* message);
void* mem_malloc(const size_t size);
void* mem_calloc_assert(const size_t nmemb, const size_t size, const char* message);
void* mem_calloc(const size_t nmemb, const size_t size);
void mem_free(void* ptr);
void mem_report(FILE* fp, const char* message);
int mem_net(void);
```

See `mem.h` for a full description of the behavior and interface to each function.

You don't have to use the `mem` module, but you'll see an example of its use in the `bag` module; we have found it helpful in debugging (and testing, and verifying) code to ensure the number of calls to `free()` are balanced with respect to the number of calls to `malloc()`.

It also provides a useful *assert* feature.
None of our Lab 3 starter code uses this feature, so here's an example.
The `bagtest.c` code begins with the creation of a bag:

```c
  // create a bag                                                               
  bag = bag_new();
  if (bag == NULL) {
    fprintf(stderr, "bag_new failed\n");
    return 1;
  }
```

That could have been written using `mem_assert` as follows:

```c
  // create a bag                                                               
  bag = mem_assert(bag_new(), "bag_new");
```

This function acts just as a pass-through for the pointer seen in its first argument... unless that pointer is `NULL`, in which case the function prints a message to `stderr` and exits.
It is useful for situations where the pointer "should never be NULL" and it is acceptable for the program to exit as a crude means of handling that unexpected condition.
It is particularly useful for code deep inside modules.
