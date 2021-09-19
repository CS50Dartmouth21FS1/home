To work with arrays in C you need to ensure you allocate space for the array, and its elements.

## Allocating an array

The simplest way to allocate an array is to declare it with a fixed size.
The most common example is an array of characters,

```c
char line[81];  // space for an 80-character string
```

but any type can be used for an array; here, an array of 100 students:

```c
struct student {
	char name[20];
	int grade;
};
struct student students[100];
```

Or here a simple array of integers:

```c
int buckets[10];
```

In this case, the array is fully allocated.
What if you don't know the size of the array (number of array elements) in advance)?
Use `malloc` or `calloc`.
For example:


```c
struct student* students = calloc(numStudents, sizeof(struct student));
int* buckets = calloc(numBuckets, sizeof(int));
```

Notice that, in both cases, we use `sizeof` with the type of array element.
By using `calloc` we get the side benefit of zeroing all the elements.
*In neither case do we need to malloc the individual array elements;*
they've already been allocated as part of this single chunk.
So now we can assign directly to the new array elements:

```c
for (int s = 0; s < numStudents; s++) {
	strcpy(students[s].name, "Dr. Seuss");
	students[s].grade = 95;
}
for (int i = 0; i < numBuckets; i++) {
	buckets[i] = 42;
}
```

We later need to free each chunk we allocated.
We only allocated space for the array itself:

```c
free(students);
free(buckets);
```

## Array of pointers

Array elements can also be pointers.
In other words, an array of pointers to something else.
A common example is an array of pointers to strings.

In [sorter1.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/sorter1.c) we saw an array of strings declared as a two-dimensional array of characters:

```c
const int maxLines = 100;   // maximum number of lines                        
const int maxLength = 50;   // maximum length of a line (including null char) 
char lines[maxLines][maxLength];
```

This array consumes `maxLines * maxLength * sizeof(char)` bytes.

In [sorter2.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/sorter2.c) we saw an array of strings declared as a one-dimensional array of pointers to char (aka, pointer to a string):

```c
const int maxLines = 100;   // maximum number of lines                        
char* lines[maxLines];      // array of lines, each a pointer to string       
```

This array consumes `maxLines * sizeof(char*)` bytes.
The array holds `char*`, and thus holds pointers discovered in other ways: most commonly, produced by `malloc`.
(In this example, the pointers are returned by `readlinep()`.)

Suppose we don't know the maximum number of lines in advance.
How do we declare (and allocate!) an array of unknown size?

```c
int numLines;    // number of lines                        
char** lines;    // array of lines, each a pointer to string       
```

The `char**` indicates that `lines` is a pointer to a pointer to a `char`... just as it was before, but now we have not yet allocated any space to hold that array.
Suppose we figure out the number of lines by some computation; then we can use `malloc` or `calloc` to allocate space for this array.

```c
numLines = ... some computation;
lines = calloc(numLines, sizeof(char*)); // each entry holds a char*
```

Now, we can assign pointers into `lines[i]`  for `i` from 0 to `numLines-1`, much as we would have with the `sorter2` case above.
For example,

```c
for (int i=0; i < numLines; i++) {
	lines[i] = readlinep();
}
```

In this case, we would later need to free each pointer in the array (because that is the contract with `readlinep`), and the array itself.

```c
for (int i=0; i < numLines; i++) {
	free(lines[i]);  // corresponds to the readlinep()
}
free(lines);  // corresponds to the calloc()
```

Of course, the decision about whether to `free` the pointers stored in the array depends on where you found those pointers.
Not all pointers come from `malloc` and not all are your responsibility to free!
For a silly example, suppose you stashed all the program arguments in an array.

```c
main(const int argc, const char* argv[])
{
	char** args = calloc(argc, sizeof(char*));
	for (int i=0; i < argc; i++) {
		args[i] = argv[i];
	}
	...
	for (int i=0; i < argc; i++) {
		free(args[i]); // wrong!!! this pointer is not from malloc
	}
}
```

### Arrays of other types

In the hashtable, you'll create an array of pointers to sets.
If the number of slots were fixed (`const`) you could do it this way:

```c
struct set* table[numSlots];  // array of numSlots entries, each a pointer to a 'struct set'.
```

If it's not, you'll need to use the `calloc` approach above.
Either way, you'll then need to allocate a set for each entry, and save those pointers in the array.

Ultimately, you can create arrays of pretty much any type,
and arrays of pointers to any type!

### Example

Here's a more complete snippet of code to hint at how to allocate an array of `num` slots.

```c
// the number of elements in the array
int num = ...; // somehow obtained; below we assume num>=2
...
// array of ints
int* array = calloc(num, sizeof(int));
array[1] = 42;

// array of pointers to int
int** array = calloc(num, sizeof(int*));// here, size of a pointer to an int
array[1] = malloc(sizeof(int)); // here, size of a whole int
*array[1] = 42;

// for other types, the process is analogous
typedef struct foo foo_t;
foo_t** array = calloc(num, sizeof(foo_t*); // here, size of a pointer to a foo
array[1] = malloc(sizeof(foo_t));  // here, size of a whole foo
array[1]->foomember = 42; // assuming foomember is an integer member of foo_t

// or, sometimes, you don't know how to initialize the structure
// because it provides a 'constructor' function. first step is the same:
foo_t** array = calloc(num, sizeof(foo_t*);
array[1] = foo_new(42); // let the constructor allocate and initialize a new foo; here we assume it returns foo_t*.
```

**Warning:** for brevity the above code does not check for NULL return value from `calloc` or `malloc`; all good CS50 code should do so!


## Equivalence of pointers and arrays

In the C language, arrays are closely related to pointers.
That is, the name of an array is actually just the address of the first element, and the application of a subscript is just another way of adding to that address; thus if `a` is an array, `a` is the address of (a pointer to) the array's first element and `a[3]` is equivalent to `a+3`, the address of (a pointer to) the fourth element (because array subscripts are based at zero, the elements are at addresses `a+0`, `a+1`, `a+2`, `a+3`, etc.).

It can be confusing.
What's the difference between these two declarations for an array holding ints?

```c
int x[];
int* x;
```

or for an array holding pointers to ints,

```c
int* y[];
int** y;
```

Not much, and the details are quite subtle.
For more detail, check the Harbison & Steele book, sections 4.5.3 and 5.4.

Personally, we recommend avoiding the first approach, despite its intuitive appeal, and stick with the second approach - because it helps to remember that 
`x` is a pointer to an `int` and
`y` is a pointer to a pointer to an `int`.
