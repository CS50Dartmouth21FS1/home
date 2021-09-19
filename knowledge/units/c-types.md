In C, every constant, variable, or function name is declared to be of a certain type.
This type may be either a base type supported by the C language itself, or a user-defined type consisting of elements drawn from C's set of base types.

## Basic types

Here are C's basic types:

| type  | description |
| :---- | :---------- |
| `void`  | the void type |
| `char`  | the character type |
| `short` | the short integer type (sometimes shorter than `int`) |
| `int`   | the standard integer type |
| `long`  | the longer integer type (sometimes longer than `int`) |
| `bool`  | the Boolean type, representing true/false |
| `float` | the standard floating-point (real) type |
| `double` | the extra precision floating-point type |
| `long double` | the super precision floating-point type |

> To use `bool` one must `#include <stdbool.h>`.

We can determine the number of bytes required for datatypes (and other things, as we will see later) with the `sizeof` operator.
In contrast, Java defines how long each datatype may be.
In C, the sizes vary from machine to machine, with the details managed by the compiler.
C's only guarantee is that:

    sizeof(char) <= sizeof(short) <= sizeof(int) <= sizeof(long)

Examine [data-types.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/data-types.c), which defines variables of each of the base types and prints the `sizeof` each one.

```bash
$ mygcc data-types.c -o data-types
$ ./data-types 
-------contents ------- sizeof()------

contents of char is a --- sizeof 1 bytes

contents int is  2 --- sizeof  4 bytes

contents short is  3 --- sizeof 2 bytes

contents long is 4 --- sizeof 8 bytes

contents bool is 1 --- sizeof 1 bytes

contents float is 1000.256714 --- sizeof 4 bytes

contents double is  1.100000e+24 --- sizeof 8 bytes

contents long double is  1E+31 --- sizeof 16 bytes
$ 
```

The above ran on `plank.thayer.dartmouth.edu`.
Although these sizes are common for Linux machines today (2021), it is possible they may change in the future; code should not be dependent on specific sizes.

## void

The `void` type is different than all of the others, in that it is not possible to define and use a `void` variable.
As such, it has no size.
It is used for two purposes:
(1) to indicate that a function returns no value, or
(2) to indicate (with `void*`) a "pointer to anything".
We'll see examples of both.

## Type coercion

C permits assignments and parameter passing between variables of different types using *type casts* or *coercion*.

For example,

```c
int a = 4;
float b = 0.0;

b = a;  // implicitly converts from int to float
a = b;  // implicitly converts from float to int
a = (int) b; // explicit "cast" from float to int 
```

Some casts in C must be made explicit, and are used where some languages require a 'transfer function'.
We will see examples of C's cast operator later in the course.

## Unsigned integers

Normal integers (`int`, `short`, `long`) can represent both negative and positive numbers.
For example, we saw above that a `short` is 2 bytes in size, thus 16 bits; it can represent -32,767 .. +32,768 (plus or minus 2^15).
But an `unsigned short`, still 16 bits in size, represents only non-negative integers, thus from 0 .. 65,535 (2^16-1).

Programs use unsigned integers when they need double the precision and never need to represent a negative value.

## <a id="const">Constant variables</a>

Although a *constant variable* sounds like an oxymoron, and it is, C allows us to define just such a thing.
Some examples:

```c
const float pi = 3.14159535;
const int maxNameLength = 50;

int main(const int argc, const char* argv[]);
```

The `const` *storage modifier* tells the compiler that this named value cannot be changed; it is useful for constants (like the first two examples) or for declaring that a function will not change its parameter values.
In CS50 we urge the use of `const` whereever appropriate, that is, whereever the programmer expects that named value to never change... and if the code accidentally tries to change the value, the compiler will issue a warning.
Helps avoid tragic mistakes!

## <a id="typedef">User-defined types (typedef)</a>

It is often helpful to define a new type, in effect, an 'alias' for an existing type.
Doing so can make the code more readable, or help to abstract the particularities of an implementation from other code that uses the type.

For example, before C supported a Boolean type, it was common to implement it this way:

```c
typedef short boolean;   // defines a new type named 'boolean'
const boolean TRUE = 1;  // defines a new constant of type 'boolean'
const boolean FALSE = 0; // defines a new constant of type 'boolean'
```

Then one could write code with Boolean variables, e.g.,

```c
boolean success = FALSE; // defines a new variable of type 'boolean'
```

In CS50 we'll see `typedef` used more often for compound types.
Those come later.

## extern

Variables, constants, and functions can be declared `extern`, meaning that their implementation occurs in a different C source file.
It is common to see `extern` declarations in a header (`.h`) file, which when included in a source (`.c`) file allows the compiler to learn about the existence of something whose definition lays elsewhere.
The compiler makes a note of it, leaving it for the linker to resolve later.
For example, we see the following *declaration* in [readline.h](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/readline.h):

```c
extern bool readLine(char* buf, const int len);
```

and then in [readline.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/readline.c) we see the *definition* (implementation) of that function:

```c
bool 
readLine(char* buf, const int len) 
{
...
}
```

## static

The confusingly-named `static` modifier has two uses.
For global names (outside of any function), they indicate to the compiler that this name should only be visible within the current source (`.c`) file.

In CS50, we most commonly see `static` used to "hide" functions within a a source file representing a module; as a result they are not visible to other source files, and they are not part of the "interface" to the module.
In this regard, it is like the difference between "private" and "public" methods in Java.
We'll come back to this idea in more detail, later.

>The `static` modifier has another use, though you will not use it in CS50.
> Inside a function, the `static` modifier on a local variable indicate that the value of this variable should persist even after the function returns.
> This feature should be used in only special circumstances and with great care.
