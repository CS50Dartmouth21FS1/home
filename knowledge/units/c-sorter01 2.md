This unit begins a series of demonstration programs that highlight several aspects of C programming:

-  arrays, and arrays of strings
-  command-line arguments
-  opening and reading from files
-  a `freadline()` variant of `readLine()`
-  compiling code from multiple files into one program

In this unit, and similar demonstrations to follow, we envision a crude replacement for the `sort` program, which reads lines of input and then prints them back out in sorted order.
We call our version `sorter`, although we begin with a simpler effort: to read lines of input into memory, and print them back out; some later versions will sort.

[:arrow_forward: **Video of demo**](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=b5ded04d-15fe-42b7-8600-ad0600135056)

> string, array of strings,
> stdio, fopen, fclose, feof, fprintf, readLine, freadLine, getc, getchar

## sorter0

This first version of the program simply reads a list of lines into memory, and prints them back out, numbered, in the order they arrived.
It has a hard-coded limit on the length of each input line, and the number of input lines; it ignores extra characters on each line and extra lines.
Its code comprises three files, two of which you've seen in earlier demos:

* [sorter0.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/sorter0.c)
* [readline.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/readline.c)
* [readline.h](https://github.com/cs50dartmouth21FS1/examples/blob/main/readline.h)

Here are some things to notice in [sorter0.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/sorter0.c).

* the inclusion of a local include file `readline.h` - indicated as local because its filename in quotes `"readline.h"` rather than brackets, as in standard include files like `<stdio.h>`.
* use of `const` for values that will never change.
* declaration of `lines` as two-dimensional array; think of it as a one-dimensional array of strings.
* `for` loop with three parts:
	* 1: `n=0` initializer
	* 2: two reasons to end (`lines` is full, or file reaches EOF)
	* 3: empty
* `printf` loop demonstrates `%d` (decimal integer) and `%s` (string) format characters.

## sorter1

* [sorter1.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/sorter1.c)
* [freadline.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/freadline.c)
* [freadline.h](https://github.com/cs50dartmouth21FS1/examples/blob/main/freadline.h)

This example shows how to use command-line arguments, and how to open and read from a file.

* `main` actually takes arguments `argc` (an integer) and `argv` (an array of strings)
* the command name is always `argv[0]`, and subsequent arguments (if any) are `argv[1]`, ...
* `argc` is thus always at least one; `argc >= 1`.
* check number and content of arguments for validity
* use of `fopen` to open a file for reading (`"r"`)
* error messages printed to `stderr` with `fprintf`
* `fclose(fp)` later called to close the file - good practice.
* `freadline` is like `readline` except it uses `getc(fp)` instead of `getchar()` so the program can read any file, not just `stdin`
* note the type of `fp` is `FILE*`; it's a pointer to an object of type FILE.
* `FILE` and functions like `fprintf` are defined in `stdio.h`.
* the use of `char*` representing a string, as in the [unit about strings](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/c-string.md).
