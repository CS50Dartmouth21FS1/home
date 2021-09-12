<!--
   This unit was written late in the term and 'dropped in' to an early
   point in the unit sequence.  It may be better to integrate its concepts
   into the surrounding units, and to consider whether it foreshadows concepts
   not yet covered (like `typedef` or 'scope') at this point in the unit
   sequence.
 -->

We are often asked **"When do I need to use an array like `char[]`, when do I need to use a pointer like `char*`, and when do I need to use `malloc`?"**

The answer is actually the same for an array of *any* type, not just arrays of `char` used as strings, but let's look at strings first.
There are two ways to allocate space in memory for a string (an array of `char`): by declaring an array, and by malloc'ing space for an array.

1. **declare an array** if you know the size of the array at compile time, and do not need the array beyond the end of the function (or other scope; see below);
2. **malloc space for an array** if you do not know the size of the array, need to return the array (pointer) to the caller of the function allocating the array, or need to store the array pointer in a data-structure that outlives this function call.

In *both* cases the resulting array variable is a pointer, i.e., the address of the first item in the array.
Recall that pointers are just addresses in memory, as explained in the [memory unit](#unit-memory) and demonstrated in the [pointers.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/pointers.c) example.

Let's look at each of those two cases.

## Array by declaration

A string is an array of `char`, with a null character (`\0`) marking the end of the string.
Each `char` needs one byte, so you need to allocate N+1 bytes to hold an N-character string.

If you know the size of an array at the time you write the code, you can allocate an array of characters:

```c
char s[11]; // a variable 's' that can hold a string of 10 chars
```

It's better style to name a constant for the length, especially if you will use that length elsewhere:

```c
const int maxLength = 10;
char s1[maxLength+1]; // add 1 for the terminating null
```

## Array by malloc

Often, however, you don't know the size of an array at the time you write the code (and thus the compiler won't know the size of the array at "compile time").
To allocate space for an unknown-sized array, we need to allocate memory at "run time", using `malloc`.

```c
int maxLength = ...;  // some computation that determines length
char* s2 = malloc(maxLength+1); // add 1 for the terminating null
```

Notice the type of `s2` is `char*`, that is, a pointer to `char`.
Actually, that is also true for `s1` above, because the name of an array is just a pointer to (address of) the first element of the array.

The difference, however, is that your program must later call `free(s2)`.
All pointers obtained from `malloc`, and *only* pointers obtained from `malloc`, should be later passed to `free()`.

## Not just char

Thus far we've been working with arrays of `char`, specifically, arrays of `char` used as strings.

But *the same concepts apply to other types*.
If you want an array of numbers:

```c
// when you know the size in advance:
const int N = 10;
float x[N]; // array of N elements

// when you don't know the size in advance:
int N = ...; // some computation of the number of elements
float* x = malloc(N * sizeof(float));
```

Note what we pass to `malloc`: the number of elements (`N`) times the size of each element (`sizeof(float)`).
*Not the size of a pointer,* but the size of the desired array element, in this case, a `float`.

> When allocating space for an array of char, as we did for `s2` above, it is common to omit the `sizeof(char)` because it is assumed to be 1.

Maybe you have declared your own type for a CS50 project; we'll call it `fifty_t`:

```c
// when you know the size in advance:
const int N = 10;
fifty_t f[N]; // array of N elements

// when you don't know the size in advance:
int N = ...; // some computation of the number of elements
fifty_t* f = malloc(N * sizeof(fifty_t));
```

This code is identical to the `float` case above, just replacing `float` with `fifty_t`.
Even if `fifty_t` is some big `struct`, arrays work exactly the same way.
The pattern always looks like this:

```c
TYPE* arrayOfType = malloc(NUMBER * sizeof(TYPE));
```

or equivalently,

```c
TYPE* arrayOfType = calloc(NUMBER, sizeof(TYPE));
```

Notice we take the `sizeof` some `TYPE`, and get back a pointer to that type, `TYPE*` ... the following would be incorrect:

```c
TYPE* arrayOfType = malloc(NUMBER * sizeof(TYPE*));
```

Note the extra `*` at right.

If you actually *do* want an array of pointers,

```c
TYPE** arrayOfTypePointers = malloc(NUMBER * sizeof(TYPE*));
```

Note the double `**` at left and single `*` at right.


## Scope

Let's return to `s1`, allocation of an array as a local variable (i.e., on the stack).

This memory is valid as long as variable `s1` stays in scope, and *is automatically de-allocated* when `s1` goes out of scope.
If `s1` is a local variable (inside a function), its memory disappears when the function returns; thus, its address (`s1`) should not be returned by the function (e.g., as the return value of the function) nor should it be stored in a data structure that exists beyond the return from this function.
Thus, the following would lead to disaster:

```c
char*
int2string(const int x)
{
  char sx[10];
  sprintf(sx, "%d", x);
  return sx;
}
```

because `sx` is on the stack and disappears (is deallocated) when the function returns; we say the *scope* of `sx` is the function body enclosed in braces.
Although immediate use of the returned pointer *might* produce the expected result, it will likely produce garbage.

More precisely, the *scope* of a variable is the block in which it is declared, which may be narrower than a function.
For example, in the following the scope of `power` is the interior of the `for` loop; the variable (and its memory) are on the stack, and disappear after each loop iteration.

```c
  for (int i = 1; i < 1000; i *= 2) {
    const int len = 5;
    char power[len];
    snprintf(power, len, "%d", i);
    ...
  }
```

> Notice how we carefully define the length of the array with a named constant, and use `snprintf` (a variant of `sprintf`) that avoids writing more than `len-1` characters into the string.

Thus the following gives a compilation error,

```c
  for (int i = 1; i < 1000; i *= 2) {
    const int len = 5;
    char power[len];
    snprintf(power, len, "%d", i);
    ...
  }
  puts(power); // here the variable 'power' is unknown
```

but the following works fine:

```c
  for (int i = 1; i < 1000; i *= 2) {
    const int len = 5;
    char power[len];
    snprintf(power, len, "%d", i);
    ...
    puts(power); // this is fine
  }
```

and prints

```
1
2
4
8
16
32
64
128
256
512
```

To summarize: it is totally fine to allocate narrow-scope string variables, or other arrays, if you know the size of the array at compile time and do not need to retain a pointer to that array beyond the scope of the variable.
