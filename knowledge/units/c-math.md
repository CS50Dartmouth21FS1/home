Although the standard C library includes many useful functions, there is an entirely separate library with a plethora of mathematics functions.

For details, study the `man` page for the desired function, such as

* `man 3 sqrt` - square root
* `man 3 pow`  - raise a number to a power
* `man 3 cos`  - cosine

Here we give an argument `3` to `man` to ask it to look in "section 3 of the manual", because library functions are described in section 3 (whereas commands like `ls` and `bash` are in section 1).

To use any of these functions, you must include the header file in your `.c` file:

```c
#include <math.h>
```

*and* you must ask the linker to link with the math library:

```bash
$ mygcc sqrt.c -lm -o sqrt
```

The `-l` option indicates you want to link with one of the standard libraries, and the library called `m` is the math library.

Look at the example [sqrt.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/sqrt.c), and the following example run:

```bash
$ ./sqrt
$ ./sqrt 4 
sqrt(4.000000) = 2.000000
$ ./sqrt 4 10 100 x 99.y 
sqrt(4.000000) = 2.000000
sqrt(10.000000) = 3.162278
sqrt(100.000000) = 10.000000
x: invalid number
99.y: invalid number
$ echo $?
2
$
```

This program demonstrates 

* floating-point variables and the `%f` specifier for `printf()` and `scanf()`,
* use of the math function `sqrt()` from the math library,
* a defensive way to parse an argument string into a number (like the `str2int()` trick we've seen before),
* definition of variables (here, `i`, `number`, and `extra`) that are only defined within the *scope* of a loop (here, the `for` loop),
* use of a variable to track the future exit status of the program – which should be 0 on success, non-zero for any failures,
* the bash special variable `$?` that holds the exit status of the most recent command.

