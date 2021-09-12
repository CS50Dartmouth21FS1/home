C is not an object-oriented language like Java, Python, or C++.
It does not provide any language features to declare a 'class' or create and manipulate object 'instances' of that class.

But C does provide two kinds of aggregate data: arrays and structures.

## Arrays

We've seen examples with arrays of characters (aka strings) and arrays of character pointers, and even two-dimensional arrays of characters.
But one can also define arrays of other types:

```c
	char names[NumStudents][NameLength];
	int ages[NumStudents];
	float grades[NumStudents];
```

and so forth.

> Sometimes you need to understand that an array name is, in effect, a pointer to the first spot in memory where that array's data lives; thus, `ages` is of type `int*` and is a pointer to the first entry in the array, but `ages[0]` is of type `int` and is the value of that first entry in the array.
> This concept comes up mostly in arrays of characters; `names` is of type `char**`, and even `names[0]` is of type `char*`, and `names[0][1]` is of type `char`.
> In effect, `names` is an array of strings, and `names[0]` is the first string in the array; like other strings, `names[0]` is of type `char*`.

## Structs

C allows you to define an aggregate called a 'struct'; like an object, it has data members.
Unlike an object, it has no methods (function members).

Continuing our above example, we could have defined a `struct` for each student, and then an array of `structs`, as follows:

```c
   struct student {
       char name[NameLength];
       int age;
       float grade;
   };
   struct student students[NumStudents];
```

Now we can refer to the first student as `students[0]`, the second student as `students[1]`, and so forth.
We can refer to the name of the first student as `students[0].name` and the age of the second student as `students[1].age`.

If we have a *pointer to a struct,* however, we normally use `->` to dereference the pointer and refer to member of the pointee:

```c
   struct student alice;        // a single struct representing alice
   struct student *sp = &alice; // a pointer, initialized to point at alice

   // the following three all do the same thing.
   int age = alice.age;
   int age = sp->age;
   int age = (*sp).age;
```


## Linked lists

In our continuing series of examples, [sorter4.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter4.c), [sorter5.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter4.c), and [sorter6.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter6.c), we declare a `struct listnode` and use it to build a linked list.
Each list node has a pointer to a string and a pointer to the next node.
The list includes a dummy *head node* to simplify list management.
The variable `head` is a pointer to that dummy node.

```c
// A structure for each node in linked list
struct listnode {
  char* name;
  struct listnode* next;
};
struct listnode head = {NULL, NULL}; // dummy node at head of empty list
```

The above code also demonstrates the initialization of a `struct` using a comma-separated list of values within braces.

After adding three nodes, the list might look like this:

![](media/malloc/linked-list.png)

## Examples

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=b082ed1e-523a-4eb0-a606-ad0b011b85c7)**
of [sorter4.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter4.c), which revises [sorter2.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter2.c) to use linked list instead of an array.

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=a6125845-a074-46b6-8b2d-ad0b01396b0b)**
of [sorter5.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter5.c), which extends [sorter4.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter4.c) to keep the list sorted.

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ce1280c4-6bd9-49b8-ac56-ad0b013b5758)**
of [sorter6.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter6.c), which extends [sorter5.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter5.c) to remove duplicates.

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=47f9aac8-8bae-4f35-891c-ad0b013ec75b)**
of [sorter7.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter7.c), which extends [sorter6.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter6.c) to free the list at the end.

To see what changes from version to version, try something like this:

```bash
diff sorter[67].c
```

### <a id="debug-sorter6">buggy sorter6.c</a>

Huh?!
Two years ago, when I dusted off these examples (first written four years ago) I found the code crashed on one of the test inputs; apparently, the malloc/free library had detected a "double free", meaning that the program had called `free()` twice on the same pointer.
I guess it's smarter than it used to be.

So I needed to debug the program, then called `names7.c`.
I decided to record my thought process while debugging, in case you might find it helpful to see how I approach such a bug.

**[:arrow_forward: Video: debugging](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=284d8c5c-565a-40ba-93c5-acb10000603d)**.
After finding and fixing the bug, I walk through another approach that you may find useful.

The corrected program is now called [sorter6.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter6.c).

## Coding style - memory allocation

Avoid sprinkling calls to `malloc()` and `free()` throughout your code.
Instead, think about the kinds of things you need to create and destroy, and write type-specific wrappers for each such type.

For example, if your program manipulates things of type `struct listnode`, you would write two functions:

```c
    struct listnode* listnode_new(...);
    void listnode_delete(struct listnode *nodep);
```

The first function calls `malloc(sizeof(struct listnode))` and initializes all of its contents, perhaps using parameters passed by the caller.
The second calls `free(nodep)`.
Both involve careful error-checking code.
See example [sorter7.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter7.c).

There are many advantages to this approach:

* The mainline code is more readable, because it's clear what `listnode_new()` is doing.
* The `new` function acts like a 'constructor' in object-oriented languages and can ensure the newly returned memory is initialized to something reasonable (at least, not the garbage returned by `malloc`).
* Code involving `malloc` can sometimes be tricky, and you isolate that trickiness in one spot and focus on getting it right once.
* Some new types might need multiple `malloc` calls, as in our linked-list examples [sorter4.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter4.c), [sorter5.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter5.c), and [sorter6.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/sorter6.c).
All those `malloc` calls (and corresponding `free` calls) can be in the `new`/`delete` functions.
* You can insert debugging output or reference-counting logic, or set debugger breakpoints, in these `new`/`free` functions and immediately have that feature apply to *all* occurrences of your program's work with this type.
