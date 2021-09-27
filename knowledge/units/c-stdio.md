
Unlike Java and Python, the C language itself does not define any syntax for input or output.
Instead, the C library provides a standard set of functions to perform file-based input and output.
The *standard I/O library* (aka `stdio`) functions provide efficient, buffered I/O to and from both terminals and files.

Every C file using functions in the standard I/O library must include this line near the top of the source file:

```c
#include <stdio.h>
```

All transactions through the standard I/O functions require a *file pointer*:

```c
  FILE* fp;

  fp = fopen("file.txt", "r");
  ...
  fclose(fp);
```

Although a 'file pointer' is, strictly speaking, a C pointer, we don't care much about what it is - we simply pass this pointer to functions in the standard C library.

> Some texts will refer to this pointer as a *file stream* (and C++ confused this term even more), but these should not be confused with nor be described as akin to Java's "streams".

The stdio library predefines three file pointers: `stdin`, `stdout`, and `stderr`, which are already opened for you (by the shell or other program that executed your C program) and which you can read, write, or manipulate using stdio functions.

Several functions are provided to check the status of file operations on a given file pointer, including:

```c
  feof(fp)      /* is the file (fp) at the 'end of file'? */
  ferror(fp)    /* was there an error in the last action on file (fp)? */ 
```

The standard I/O functions all return `NULL` or `1` (as appropriate) when an error is detected.
Here is an example of opening a file, checking for success, and later closing the file:

```c
#include <stdio.h>

int
main(int argc, char *argv[])
{
  char* filename = "/etc/passwd";
  FILE* fp;
  int exit_code = 0;

  if ((fp=fopen("/etc/passwd", "w")) == NULL) {
    fprintf(stderr,"*** could not open file!\n");
    exit_code = 1;
  } else {
    printf( "File opened!\n");
    /* ... print to the file ... */
    fclose(fp);
  }
  return exit_code;
}
```

> In the preceding, notice how it embeds an assignment statement inside the condition of the `if` statement!
> That's ok, because an assignment statement is an expression that itself has a value - the value that is assigned to the variable on the left-hand side - and that value is then used in the outer expression (here, a conditional expression testing equality).
> Thus, the `if` statement could have been written as

```c
  fp = fopen("/etc/passwd", "w");

  if(fp == NULL) {
  ...
```

> but such a construct appears so often that they are often combined.
> When the assignment is included in the `if` condition, it was wrapped in parentheses just to be sure that it's treated as a whole, as the left-hand-side of the `==` comparison operator.

For details, `man fopen`, `man fclose`.

## printf, scanf

The most frequently used functions in the C standard I/O library perform output of formatted data.

```c
  fprintf(FILE* fp, char* format, arg1, arg2, ...);
```

for example,

```c
  int class = 50;
  char *department = "Computer Science";

  fprintf(fp, "Course: %s %02d\n", department, class);
```

The `fprintf` function is a generalization of `printf`.
Put another way, `printf(format, arg...)` is shorthand for `fprintf(stdout, format, arg...)`.


Many standard I/O functions accept a *format specifier*, which is a string describing how the following arguments are to be interpreted.
This mechanism is in contrast to Java's `toString` facility in which each object knows how to output/display itself as a `String` object.
There are many possible format specifiers, the most common ones being `c` for character values, `d` for decimal values, `f` for floating-point values, and `s` for character strings.
Format specifiers may be preceded by a number of *format modifiers*, which may further specify their data type and may indicate the width of the required output (in characters).

For details, `man 3 printf` (note the `3` to ensure `man` prints the page about the `printf` functions rather than the `printf` shell command).

The C standard I/O library provides efficient "buffering".
This means that although it appears that the output has gone to the `FILE` pointer, it may still be held within an internal character buffer in the library (and will hence not be actually written to the hard disk or to the screen until more output is accumulated and the buffer becomes full).
We often need to "flush" our output to allow us to *know* when the output will be written to disk or the screen:

```c
  /* ... format some output ...*/
  fflush(fp);
```

As you might expect, `FILE` pointers are automatically flushed when a file is closed or the process exits.

A related function `sprintf` allows us 'output' to a character array (a string):

```c
  int class = 50;
  char *department = "Computer Science";
  char buffer[BUFSIZ];

  sprintf(buffer, "Course: %s %02d\n", department, class);
```

> Security alert! What is the potential exposure in the code above?
> Read `man snprintf`.

C's standard I/O library may also be used to input values from `FILE` pointers and character arrays using `fscanf()` and `sscanf()`.
Because we want the contents of C's variables to be modified by the standard I/O functions, we need to pass them *by reference*, which in C is accomplished by passing the 'address' of the variables using the `&` operator:

```c
  fscanf(fp, format, &arg1, &arg2, ...);
```

like this:

```c
  int i, res;
  char buffer[BUFSIZ];
  fscanf(fp, "%d %d", &i, &res);
  sscanf(buffer, "%d %d", &i, &res);
```

We'll talk more about addresses and pointers in an upcoming unit.

## getchar, putchar

Below is a common idiom that reads a file character by character and, in this case, simply prints it right back out.
(It's like `cat` without command-line arguments.)
From [getput.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/getput.c):

```c
  while ((c = getchar()) != EOF) {
    putchar(c);
  }
```

Here,
`getchar()` reads a single character from stdin,
`putchar()` writes a single character to stdout;
you could of course include other logic within the loop.

> Similar functions `fgetc()` and `fputc()` allow you to specify a FILE for input or output.

Notice how the character variable `c` is assigned the return value of `getchar()` within a parenthetical expression `(c = getchar())`; the value of that expression is the same assigned value, and thus it is *that* value compared against the stdio-defined constant `EOF`.
Because `getchar()` returns `EOF` on end of file or error, this loop eventually terminates.

For details on this family of functions, `man 3 getchar`.

## Choose the right approach

Sometimes, it is convenient to read input line by line; for that we recommend the `readline()` function from [readline.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/readline.c).

Other times, it is more convenient for the logic of the program to read input character by character, as above.

Finally, there are times when the input can be assumed to be clean, such as a sequence of numbers or words; in those rare cases, it may be convenient to loop over calls to `scanf()`.


## Check for errors

When using the C standard I/O functions, we must pay attention to the particular return values from those functions.
The functions themselves may return `NULL` file pointers, the number of input items read, the number of characters written, or special non-zero values that indicate the end of a file or some other error condition.
Always check the `man` page to be sure.
Some of the special return values have constants defined in the system include files for convenience, improved code readability, and consistency.
