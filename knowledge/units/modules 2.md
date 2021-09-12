Some data structures are so common that it is valuable to code them once - lists, queues, stacks, trees, and hash tables - and then re-use that code for multiple programs (or multiple purposes within a program).
Code re-use saves time because you don't need to write everything from scratch.
By using robust, well-tested modules rather than fresh (buggy) code, your program is more reliable.
Finally, by separating 'business logic' from 'data structures', your code is clearer and more flexible (e.g., if you later want to switch to a more-efficient data structure you can do so without rewriting all the business logic).

Object-oriented languages make this easier, because they make it simple to define a 'class' of objects and then to create new 'instances' as needed.
Many such languages go further by providing 'templates' and 'subclasses' as a way to derive new variants of the base class.

C has none of these capabilities.
But we can approximate some of these concepts through good style and careful design.

### Pointers to anything

In developing a general-purpose data-structure module, we would really like the module to be able to store arbitrary "things" -- not just predetermined types -- but anything.
For example, instead of a linked-list of strings (as we built in the `sorter` series of programs) we'd like a linked-list that could store pointers to anything.
Today we want a list of string things; tomorrow we might want a list of `struct student` things.

The solution is for the module to store *pointers to things*, rather than the things themselves.
The user's contract with the module is thus "I allocate and initialize a thing, and give you a pointer to remember; later, when I need that thing, I'll ask and you give me back that pointer.
When I ask you to print things, or delete things, I'll give you customized functions that know how to print and delete things of this type."

Java and other object-oriented languages do this with *templates*.
C has no support for templates, but it does have a type for "pointer to anything":  `void*`.
Thus,

```c
char* p = "hello";	       // pointer to a char, in this case, a string
int x = 42; int *xp = &x;  // pointer to an int
struct student* sp;        // pointer to a struct
sp = malloc(sizeof(struct student)); // initialize the pointer
sp->name = "David";       // initialize the struct at that pointer
sp->house = "West";       // ...initialize
sp->class = 1986;         // ...initialize

void* anything;       // a pointer to any type
anything = p;         // here, a pointer to a char on the stack
anything = &x;        // here, a pointer to an int on the stack
anything = sp;        // here, a pointer to a struct in the heap
```

Thus, our modules will accept and store `void*` pointers, and return them to the caller when asked.

### Pointers to functions

As noted above, the module may need help when it needs to print, compare, or delete "things".
The module's user must provide the module with special helper functions.
(In an object-oriented language, like Java, "things" are objects and objects know how to print themselves, compare themselves, or delete themselves.
C has no such support.)

Thus, the caller may pass a function to the module, when needed.
We can't actually pass the function - we have to pass the module a pointer to the function.

The concept of a *pointer to a function* can be confusing.
Like any other pointer, it is an address in memory.
Recall that the compiled code of the program lives in the code segment, in memory, so every function resides at some address.
A function pointer is simply that address.

We can refer to the function's address simply by naming the function, without the syntax of calling a function.
That is, `foo` is, by itself, the address of a function, whereas `foo(a, b, c)` calls that function and passes arguments `a`, `b`, and `c`.
In our [pointer2.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/pointer2.c) example, we passed the address of functions `main` and `change` to `printf` so it could print those addresses for us to examine.

If I have a function called `myPrint`, like this:

```c
void myPrint(FILE* fp, char* key, void* item)
{
	int* valuep = item; // in this case, the "things" are integers
	fprintf(fp, "(%s, %d)", key, *valuep); // note *valuep derefs pointer
}
```

If I have a function pointer `printFunc`, I can initialize the function pointer and call through that pointer as follows:

```c
printFunc = myPrint;
(*printFunc)(fp, key, thing);
```

In other words, I dereference the pointer to get a function, and then call that function.
Notice the `*` and parens, `(*printFunc)`.

How would I declare that variable `printFunc`?
pretty gnarly syntax:

```c
void (*printFunc)(FILE* fp, char* key, void* item);
```

declares variable `printFunc` to be a pointer to a function whose prototype looks like that of `myPrint` above, that is, it takes the given three arguments and returns `void`.
Indeed, it looks almost like a common function prototype, except for that `(*variableName)` notation.
For this pointer to be useful, it must be initialized to the address of a function.

### typedefs

C allows us to give a new name for a type; we most often see this for structure types.
In CS50 our naming convention appends `_t` to the name of a type.
Thus, for example,

```c
struct student {
	char *name;
	char *house;
	int class;
};
typedef struct student student_t;
student_t st;
```

Here we define a type called `student_t`, identical to the type called `struct student`, and a variable (of that type) called `st`.
It is so common to define a struct type and immediately give it a typedef alias that one often sees it written in shorter form:

```c
typedef struct {
	char *name;
	char *house;
	int class;
} student_t;
student_t st;
```

Here the `struct` type itself is not even named, but the typedef is formed and can be used to define variables of that type.

## Example - bags

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=3a2f1124-d3d0-4b37-bcb0-ad0b0147e91d)**

Here, we live-code a *bag* data structure and use it to make a new version of our `sorter` series.
We start from [sorter5.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter5.c) and modify it with the goal of coding functions that manipulate a generic "bag of things".

> The video is long, but builds up the entire module from scratch and talks through *why* each approach is taken.
> The resulting implementation is slightly different than the resulting clean version of the code, linked below; I encourage you to study those files and note the comments and other stylistic aspects of a complete module.

A *bag* is an unordered collection of (items).
The bag starts empty, grows as the caller adds one item at a time, and shrinks as the caller extracts one item at a time.
It could be empty, or could contain hundreds of items.
Items are indistinguishable, so the *extract* function is free to return any item from the bag.

> Admittedly, the semantics of a bag seems incongruous with a program called `sorter`, and it is, because it doesn't even remotely help to sort the lines.
> It still serves to demonstrate a useful data structure (bag) and the concept of a module.

The example demonstrates

 * a *module*, which is as close as we get to a *class* in C.
 * A set of functions exported via `bag8.h` to other C files.
 * Structure types (like `struct bag` and `struct bagnode`).
 * Public types (like `bag_t`) - and private types (like `bagnode_t`).
 * Private functions (like `bagnode_new`).
 * The use of an *opaque type* like `bag_t`: the module's source code (`bag8.c`) defines its internals, but module users see only the interface in `bag8.h`, which simply indicates it is a struct.
 * No need for global variables.  (We always try to avoid them!)
 * Heap memory (created via `malloc` in `bag_new` and `bagnode_new`).
 * The use of pointers to build and manipulate a bag data structure.
 * The use of `void*` to represent "a pointer to anything" and its use to build a bag of generic things.

The complete program, after some cleanup, is in 
[sorter8.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter8.c),
[bag8.h](https://github.com/CS50Dartmouth21FS1/examples/blob/main/bag8.h),
[bag8.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/bag8.c).

A complete *bag* module is included in the [Lab 3 starter kit](https://github.com/CS50Dartmouth21FS1/home/blob/main/labs/lab3).

## Modules

So, after all that, what is a *module*?
C has no specific construct or concept of a 'module'.
In CS50, we think of a module as an abstraction – such as a data structure – that is implemented by a separate set of code that abstracts away (hides) the details of the implementation of that abstraction, while providing a clean, clear interface.
In C, such an implementation would be comprised by one or more source files (`.c` files) and the interface defined by a header file (`.h` file).
The `bag` is the first good example; in Lab 3 we provide the bag and two other modules (`mem` and `file`) and you'll write three others (a counter set, a set, and a hashtable).
