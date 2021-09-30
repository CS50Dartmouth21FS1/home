The first few units have been a crash course in the shell and shell programming.
Now we move to the C language.
We now begin developing our C programming skill set by first understanding the basics of the language and then (through examples) study good code and write our own.
This first unit serves as an introduction to the C language:

- Structure of a C program
- Compiling and running a C program
- Input and output with stdin, stdout
- Random numbers
- Functions

## C

C can be correctly described as a successful, general-purpose programming language, a description also given to Java and C++.
C is a *procedural* programming language, not an *object-oriented language* like Java or C++, and not a *functional programming language* like Haskell or Clojure.
Programs written in C can be described as 'good' programs if they are written clearly, make use of high-level programming practices, and are well-documented with sufficient comments and meaningful variable names.
Of course, all of these properties are independent of C and are traits of good programming in any high-level language.

C has the high-level programming features provided by most procedural programming languages - strongly typed variables, constants, structured types, enumerated types, a mechanism for defining your own types, aggregate structures, control structures, recursion and program modularization.

C does not support sets of data, Java's concept of a *class* or objects, nested functions, nor subrange types and their use as array subscripts, and has only recently added a Boolean datatype.

C does have, however, separate compilation, conditional compilation, bitwise operators, pointer arithmetic and language independent input and output.
The decision about whether C, C++, or Java is the best general-purpose programming language (if that question can be decided, or even needs to be decided), is not going to be an easy one.

C is the programming language of choice for most systems-level, engineering, and scientific programming.
The world's popular operating systems - Linux, Windows, and Mac OS X, are written in C; the infrastructure of the Internet, including most of its networking protocols, web servers, and email systems, are written in C; software libraries providing graphical interfaces and tools, and efficient numerical, statistical, encryption, and compression algorithms, are written in C; and the software for most embedded devices, including those in cars, aircraft, robots, smart appliances, sensors, and game consoles, is written in C.
Recent mobile devices such as the iPhone, iPad, and some Microsoft products use languages derived from C, such as Objective C, C#, and Swift.

C's overall philosophy is "get out of the programmer's way."
Sometimes that's just what the programmer needs; on the other hand, C is often criticized for allowing the programmer to do pretty much whatever she wants.

The shell scripts we wrote are interpreted, rather than compiled.
Interpreted languages are fed (in source code) to an *interpreter* that checks and executes it immediately.
A *compiler*, on the other hand, checks the code and translates it to a simple code called Assembly language, which is itself then translated ("assembled") into machine code (aka 'binary' code).


## Examples

In class we iteratively build a C version of our friend [guess1a.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/main/guess1a.sh), and then enhance it.

> ***NOTE: if the `mygcc` command doesn't work for you on plank,***
> you may have overlooked one of the setup steps in the 'Systems' section of
> [Lab0](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/labs/lab0), specifically,

  ```bash
  echo source ~/cs50-dev/dotfiles/bashrc.cs50 >> ~/.bashrc
  echo source ~/cs50-dev/dotfiles/profile.cs50 >> ~/.profile
  ```
> After these commands, log out of plank, log back in, and try again.

**Advice:**
watch the video first, then look at the linked examples.
The video shows the iterative development of ever-better code for the guessing game, with verbal explanations along the way.
You can later study, copy, and play with the example code for further elucidation.
The examples include better commenting than in the video, which skipped over some commenting to keep the videos shorter.

**[:arrow_forward: Video about guess1,2,3](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c30c9ea4-e481-4849-9c81-ad0100b02488)**

**[:arrow_forward: Video about guess4,5,6](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6aaee5ab-44c2-4065-8458-ad0100b5ae04)**

As you work through the C versions below, note you can see what changes from one version to the next using `diff` (here, I use the `[]` filename globbing syntax in bash):

```diff
$ diff guess[23].c
2,3c2,3
<  * guess2.c - a C version of our simple bash demo program guess1
<  *  (we guide the user toward an answer.)
---
>  * guess3.c - a C version of our simple bash demo program guess1
>  *  (we move the repeated code into a function.)
9a10,26
> /* ***************** askGuess ********************** */
> /*
>  * Ask the user to guess, and return the guess.
>  *  where [low-high] is the range of numbers in which they should guess.
>  */
> int
> askGuess(const int low, const int high)
> {
>   int guess;
> 
>   printf("Guess my number (between %d and %d): ", low, high);
>   scanf("%d", &guess);
> 
>   return guess;
> }
> 
> /* ***************** main ********************** */
17,18c34
<   printf("Guess my number: ");
<   scanf("%d", &guess);
---
>   guess = askGuess(1, 100);
26,27c42
<     printf("Guess my number: ");
<     scanf("%d", &guess);
---
>     guess = askGuess(1, 100);
```


## guess1

> see video demo above

[guess1.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/guess1.c): (simple replacement for the bash program)

* notice the `#include` files, which textually include some standard C definitions into the code there.
* specifically, we always include `stdio.h` and `stdlib.h` because they provide important library functions.
* notice the `main()` function must always exist for a complete program.
* notice that `main` returns an integer - which will be the exit status of the program.
* notice we use `const` to indicate the variable is a constant - its value can never change.
* `printf` prints to stdout with a format, but here it just prints a simple string.  `\n` refers to newline.
* `scanf` reads stdin using a format, here indicating a decimal integer (`%d`)


## guess2

> see video demo above

[guess2.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/guess2.c): (provide the user guidance toward answer)

* we just add an `if` statement to provide guidance.
* we always use curly brackets for nesting, though they are not always required.

## guess3

> see video demo above

[guess3.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/guess3.c): (move the repeated code into a function)

* we move the repeated ask/input code to a function.
* we place the function before `main()` because otherwise `main` will not know it exists.
* we enhance the prompt to the user, adding information about the valid range.
* we use `const int` for the parameters to indicate those values cannot change within the function.

## guess4

> see video demo above

[guess4.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/guess4.c): (move the function to the bottom, so main() stays at top)

* we move the function to the bottom, allowing `main()` to stay at the top, for readability.
* to do so, we must *declare* the function `askGuess` before `main()` so `main` knows it exists.
* the declaration is a *function prototype*, meaning, everything except the body of the function.

## guess5

> see video demo above

[guess5.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/guess5.c): (add command-line arguments, check validity of guess, allow them to quit early)

* we add command-line arguments, which arrive as parameters to `main()`: an array of strings, and a number of arguments.
* the command name is always `argv[0]`, and subsequent arguments (if any) are `argv[1]`, ...
* `argc` is thus always at least one; `argc >= 1`.
* we carefully check the argument count and content.
* we use `sscanf`, which is like `scanf` except it reads from a string, to parse the first arg into an integer.
* we also pick a random answer, using `rand()`.
* but for it to be truly random, we must *seed* the randum-number generator with a different seed on every run.
* one simple way to get a different seed is to provide the current date/time - which is returned by `time()`.
* we also check for various types of error in user's input/guess.


## <a id="guess6">guess6</a>

> see video demo above

[guess6.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/guess6.c): (read input as a string, convert to integer, check input more carefully)

This example takes a major step forward, by leveraging a separate module I've written, in `readline.c` and `readline.h`.
(See the unit about the [compilation process](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/c-compile.md).)
That module provides a function `readLine()`, which fills a string (character array) with the contents of the whole line (or as much as fits).
We use it to read the user's whole input line as a string, then convert that string to an integer.
There is a lot to see here!
Let's go through some highlights.


```c
...
#include <stdbool.h>
#include "readline.h" // informs C about functions in readline.c file.

bool str2int(const char string[], int* number);
...
```

C now includes a Boolean data type, but to use it you must `#include <stdbool.h>`.
To inform C of the presence of the `readLine()` function, we must include my `readline.h` file.
(More on such files, another day.)
The `str2int` function will convert the given string to a number and return true if it succeeds.

```c
  printf("Guess my number (between %d and %d, or 0 to give up): ", low, high);

  // use readLine() to read a whole line, or as much as fits into our buffer
  // if it returns false, the line was too long, or an EOF occurred. 
  if (!readLine(inputLine, bufsize)) {
    if (feof(stdin)) {    // end of file
      printf("EOF\n");
      return 0;
    } else {      // error in input
      return -1;
    }
  }
```

The `askGuess` function can now use `readLine` to read the line as a string.
We check its return value to see whether it succeeded.
If failure, then we check for EOF; in that case, assume the user wants to quit and return 0.

Since we now read a string, we can compare it against strings; here we allow the user to type "quit" to exit.

```c
  // now they can type 'quit' or '0' to exit.
  if (strcmp(inputline, "quit") == 0) {
    return 0;
  }
```

But otherwise, we should look for an integer.
We use our new `str2int` function to convert.
If it returns false, then `!str2int(..)` is true, and return with error.

```c
  // now they can type 'quit' or '0' to exit.
  if (strcmp(inputLine, "quit") == 0) {
    return 0;
  }
...
```

Now, how do we convert a string to an integer?  
We used `sscanf` before, but let's look more closely.

```c
/* ***************** str2int ********************** */
/*
 * Convert a string to an integer, returning that integer.
 * Returns true if successful, or false if any error. 
 * It is an error if there is any additional character beyond the integer.
 * Assumes number is a valid pointer.
 */
bool str2int(const char string[], int* number)
{
  // The following is one of my favorite tricks.
  // We use sscanf() to parse a number, expecting there to be no following
  // character ... but if there is, the input is invalid.
  // For example, 1234x will be invalid, as would 12.34 or just x.
  char nextchar;
  return (sscanf(string, "%d%c", number, &nextchar) == 1);
}
```

Here I ask `sscanf` to look for an integer followed by a character.
Normally, it should find just the integer, and return 1 indicating it was able to retrieve just one item (the integer).
But if it returns 0, there was no valid integer; if it returns 2, there was a valid integer and some character afterward.
Both would be an error.
We return the result (true/false) of the comparison between `sscanf` return value and `1`.

This function gets a little ahead of ourselves... because `number` here is a *pointer*, the results of passing `&guess` to the function.
We'll come back to explain pointers later.

## atoi

Another example, which explores the same `sscanf` trick used in `guess6`, is [atoi.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/atoi.c).

There actually is a function `atoi()` in the standard C library, but I find it too weak; it does not adequately catch and indicate errors in translation, like the approach in our version.
