# Solution - Mad Libs

Regarding the [madlib.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/madlib.c) code, here are some **things you should notice:**

* The file header comment - giving name and usage of the program, assumptions, exit status, author, date.
* the `include` files for standard libraries and our custom `readline` function.
* the use of global variables - here, constants
* the function prototype for `fillTemplate()`
* the arguments `argc` and `argv` passed to `main()`
	* we mark them as `const` because we do not intend to change either value
	* because we can always assume `argc >= 1`, we can safely use `argv[0]` -- it is always the name of the command used to run the program. I save a pointer to this string in `progName` for readability.
* we check to see whether the correct number of arguments were provided
	* if not, we print a helpful 'usage' message
	* if so, we save a pointer to the two arguments in variables with descriptive names, to make the code more readable
* we open the template file for reading ("r")
* we open the output file for writing ("w")
* the use of an assignment statement as a value
	* the statement `x = 5` is actually an expression with value 5.
	* thus, we can use it anywhere a value is needed; it is best to put it inside parentheses for readability, e.g., `y = (x = 5)` assigns 5 to both x and y.
	* here we are saving the results of `fopen` in a variable *and* testing that result in an `if` statement.
	* you'll see this same form in the `while` loops below.
* useful error messages printed to stderr.
* the call to `fillTemplate()` pushes all the program logic outside `main()`, allowing `main` to focus exclusively on parsing arguments, setting up, and cleaning up - and allowing readers of `fillTemplate` to focus on the specifics of how it works without all this initialization/cleanup cruft in the way.
* remembering to close files when done with them; not strictly necessary here, because we're going to exit immediately afterward, but always a good practice.
* a useful concluding message so the user knows they're done and whether it was successful.

**In `fillTemplate()`, notice**

* the header comment that explains what this function does and what it returns
* the variable declarations with a comment about each one.
* the use of `const` to give names to certain constants.
	* this improves readability.
	* if I ever want to change them, I need only change one place.
* two strings, represented as character arrays.
* an outer loop and an inner loop
* the outer loop copies characters from input to output,
* use of `fgetc` to read a character from a file
* use of `fputc` to write a character to a file
* but if `codeStart` character appears, run the inner loop to scan characters into the `codeName` string.
* the use of `break` to exit a loop early.
* we take care not to overflow the `codeName` string.
* `n.b., this also implies i == codeLength`.
	* Note there are only two ways out of the loop above; in one case, `c == endCode` and we left the loop with `break`, and in the other case the iteration reached `i == codeLength`.
	* Thus, if `c != endCode` it must be the latter case, where `i == codeLength`.
	* Thus, we filled the `codeName` array and now need to terminate it with a null character, and handle the excess input by reading and discarding it until we find the `endCode`.
	* note the empty while-loop body.
* prompting for input, and using `readLine()` to read a line as input.
* stripping off the last character of the input string
* using `fputs` to write a string to output file
* using `feof` to test whether we actually reached end of file.
	* the loop ends when `fgetc` returns EOF, but that can happen on error as well as on end of file.
	* note here that C will *cast* an `int` (returned from `feof`) to a `bool` (as required for the return value for `fillTemplate`), that is, it will convert a value of type `int` to a value of type `bool`; specifically, zero becomes `false` and non-zero becomes `true`.

**Stylistically, notice**

* header comment at top of file
* paragraph comment for `fillTemplate`, describing its *contract* with caller
* one-line comments before blocks of code
* end-of-line comments for declarations and some code.
* clean, consistent indenting, even of the end-of-line comments.
* global constants so key parameters are visible at the top
	* these don't need to be used outside `fillTemplate` so they need not be global variables, but it is (debatably) more readable having critical parameters at the top.
* curly braces around `if` clauses and loop bodies.
* use of camelCase for variable and function names.
* meaningful variable and function names.
* factorization of program into `main` and `fillTemplate`
* error checking (within the limits of our assumptions)
* naming constants rather than sprinkling them throughout.


## Extension to print the result

My solution is in [madlibprint.c](madlibprint.c),
and is inspired by [getput.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/getput.c).
The core function is as follows:

```c
/* ********************* printFile ************************ */
/* printFile:
 * Copy characters from the given (already open) file to stdout.
 * Returns nothing (hence, "void").
 */
void printFile(FILE *fp)
{
  char c;
  while ((c = fgetc(fp)) != EOF)
    putchar(c);
}
```

and it is called from `main()`, as follows, if `success`:

```c
  if (success) {
    // open output file for *reading*
    if ((in = fopen(outputFilename, "r")) == NULL) {
      fprintf(stderr, "%s: cannot re-open output file '%s'\n", 
	      progName, outputFilename);
      exit(2);
    }

    printf("\n---------------------------------\n");

    printFile(in);

    exit(0);
  } ...
```
In this unit, we continue our accelerated introduction to the C programming language.

