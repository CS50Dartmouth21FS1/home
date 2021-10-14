# Activity - Mad Libs

Some of you may have encountered a book of [*Mad Libs*](https://www.madlibs.com); on each page is a brief story with some words left blank.
One player sees the story and asks the other player, who cannot see the story, to fill in the blanks; each blank is labeled simply *noun*, *verb*, or perhaps something more explicit like *fruit* or *color*.
After all the blanks are filled, the first player reads the story aloud, which can be pretty funny!

Let's look at a program that allows you to play Mad Libs!
All the files you need are in the examples directory `~/cs50-dev/shared/examples`,
also visible in the [examples repo](https://github.com/CS50Dartmouth21FS1/examples).

1. Compile the example `madlib.c.`
2. Play it once on your own, using the example input `madlib1.txt`; afterward, look at the input and output files.
2. Play again as a team; one person run it on `madlib2.txt` -- don't peek at the file! -- and all teammates can call out the words to input.  Read the output file aloud!
1. Study the code for `madlib.c`.
  Make a list of questions to ask the professor.
  Try to answer these questions:  
      1. Notice the "global constants" at the top of the file; why are they defined there? why are they defined at all?
      2. Why is `const` applied to the parameters of `main` and its three string variables?
      3. Indeed, those three string variables are not strictly necessary - so why does the code define them?
      4. Notice that two `fopen` calls occur within the conditional expression of an `if` statement - why and how does that work?
      5. Notice that `main` does none of the actual program logic, which is deferred to a function `fillTemplate()`; why?
      6. Notice that the return value of `fillTemplate()` is captured in a variable, rather than being tested directly/immediately by an `if` statement; why?
      7. Inside `fillTemplate()`, notice how the `while` loop scans the whole file - and is another example of an assignment statement within a conditional expression.
      8. How does the code detect and extract `<code>` from the input?
      9. Why is this comment true:  `n.b., this also implies i == codeLength` ? (n.b. means *note bene*, Latin for "please note")
      10. What is `'\0'` and why is it used?
      11. Why does the code need to truncate an over-long substitution provided by the human player? why is that not a fatal error?
      12. Notice the calls to `fputs()` and `fputc()`, and the code checking for error returns; why?
      13. The final line of code calls `feof()`, a function that returns an `int` (read the man page), and returns its value directly as the result of our function `fillAdapt()`.  But our function returns `bool`, not `int`; why does this work?

1. If you have time, extend a copy of `madlib.c` to print out the resulting story when translation is finished (and successful).
  It might be nice to print a blank line and a row of dashes so the story is nicely set off from the prompts & answers in the first phase of the program.
  You should write a new function `printFile(FILE *fp)` that simply copies characters from that file (already open for reading as `fp`) to stdout.
  `main` can thus open the outputFileName (for reading) and call this function.
  I anticipate this function will be only 2-3 lines long!
