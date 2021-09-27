As we've seen, the user of a shell (like bash) can specify zero or more *command-line arguments* for each *command* - and most commands are simply programs.
Some programs are scripts interpreted by other programs (like bash or python), and other programs are machine-code binaries compiled from another language (like C or C++).
In any case, the shell asks the operating system (OS) to execute the program, and passes along the arguments.
When the OS executes a compiled C program, it calls the function `main()` with two parameters:

1.  an integer argument count (conventionally called `argc`),
2.  an array of pointers to character strings (conventionally called `argv`)

> Notice that in many previous examples we've provided a `main()` without any parameters all.
> C does not check the length and types of parameter lists of functions for which it does not know a prototype.

> In addition, the function `main()` has no special significance to the C compiler.
> Only the linker requires `main()` as the apparent starting point of any program.

Most C programs you see will look like this:

```c
int main(int argc, char* argv[])
```

I prefer to declare them as constant so we let the C compiler help us avoid modifying these input parameters:

```c
int main(const int argc, const char* argv[])
```

The following program prints out its command line.
Note that `argv[0]` is the command name and `argv[1]` ... `argv[argc-1]` are the command-line arguments (after any expansion or subsitutions done by the shell).

Look at a quick run of the tiny program [arguments.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/arguments.c):

```bash
$ mygcc arguments.c -o args
$ ./args
1 arguments:
0: ./args
$ ./args a b c "d f k"
5 arguments:
0: ./args
1: a
2: b
3: c
4: d f k
$ 
```

We declared `argv` as *array of pointers to char*.
For any given argument *i*, `argv[i]` is one of those pointers; that is, `argv[i]` is of type `char*`.
We can pass that pointer to functions like `printf`, wherever it expects a string.

## Command-line switches

A common activity at the start of a C program is to search the argument list for command-line switches commencing with a dash character.
The remaining command-line parameters are often assumed to be filenames.

Study the example [nosort.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/nosort.c), which shows one way to parse the command line of a hypothetical replacement for the `sort` command.
Its commandline should look like `nosort [-r] [-u] [-n] filename...`
as follows:

```bash
$ mygcc nosort.c -o nosort
$ ./nosort
$ ./nosort x
next argument 'x'
$ ./nosort -r x
reverse sort
next argument 'x'
$ ./nosort -r -n -u x y z
numeric sort
unique sort
reverse sort
next argument 'x'
$ 
```

The switches can be listed in any order, but not combined as in

```bash
nosort -run
```

Note the example's defensive programming: if the user enters a missing or bad option then the user is informed with a usage message:

```bash
$ ./nosort -
Error: missing option '-'
Usage:  ./nosort [-r] [-u] [-n] filename...
$ ./nosort -x
Error: bad option '-x'
Usage:  ./nosort [-r] [-u] [-n] filename...
$ 
```

The example also demonstrates several things about C, and C idioms:

 1. the `switch` statement and its component `case` and `break` statements.
 2. the use of `argc--; argv++` as a way of stepping through an array. Note: each time `argv` is incremented, it changes the base address on which a subscript like `[1]` is interpreted.
 3. the syntax for subscripting a two-dimensional array, like `argv[1][0]`.

> Note `argv` is not (strictly speaking) a two-dimensional array, and C does not (strictly speaking) support multi-dimensional arrays; the first subscript selects one of the `char*` pointers in the array-of-pointers that is `argv`; the second subscript selects one of the characters in the array of characters to which that pointer refers.
> Thus, `argv[1]` is a `char*` (a pointer to a character string), and by subscripting *that* further, in `argv[1][0]`, we refer to the character itself.
