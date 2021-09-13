# Lab 2 - C programming

:point_right:
Design, write, document, and test the following C programs.
In each case you will need to write a testing script.

***Point breakdown:***

  * (30 points) chill.c
  * (30 points) words.c
  * (40 points) histo.c

## Reminder

Grading will focus on [CS50 coding style](https://github.com/CS50Dartmouth21FS1/home/blob/main/labs/style.md) - including consistent formatting, selection of identifier names, and use of meaningful comments - in addition to correctness and testing.

***Your C code must compile without producing any compiler warnings.***
You will lose points if the compiler produces warnings when using our CS50-standard compiler flags (i.e., when compiled with `mygcc)`.

## Preparation

<!-- @CHANGEME update the invitation link -->

:point_right:
[Accept the assignment](https://classroom.github.com/a/-1zEw49g), and clone the repository into your `cs50-dev` work area (plank).
It will looks something like this, assuming your GitHub username is XXXXX:

```
$ cd cs50-dev/
$ git clone git@github.com:CS50Dartmouth21FS1/lab2-XXXXX.git
Cloning into 'lab2-XXXXX'...
```

*Notice that I started work by changing to my cs50-dev directory.*
The clone step will create a new directory `~/cs50-dev/lab2-XXXXX`.

:point_right:
First, edit `README.md` to remove instructions, add your name, and add your username.
You can use this file to provide any overall comments you want to convey to the grader.

> Remember that you can preview Markdown files with various Markdown-editing or -rendering tools (see: [Markdown resources](https://github.com/CS50Dartmouth21FS1/home/blob/main/logistics/systems.md#markdown)) but we will read it on GitHub.com, so before you make your final submission decision be sure to check it there.

:point_right:
Make three directories where you'll do your work:

```
$ cd cs50-dev/lab2-XXXXX
$ mkdir chill words histo
```

:point_right:
In each subdirectory, write the corresponding program as described below.
Each directory should have a `.gitignore` file, which causes git to ignore the respective binary file (e.g., `chill`, `words`, `histo`) and anything else you don't want committed.
We've provided a CS50-default `.gitignore` file in the top-level directory of the repo, which should cover most of the usual things.


-----------------------------------------------------------

## What to hand in, and how

When finished, your `lab2` directory should contain a `README.md` file and three subdirectories, each of which contain certain files as detailed below.

You should add **only** the necessary files to your repo
*(do not commit any compiled C programs!)*.
Check `git status` before you commit; it will list the files that will be committed, near the top, and the files that will not be committed, or are 'untracked', near the bottom.
*Study the status output carefully: if you miss adding a file we need, you'll lose points, and if you add a scratch or binary file that should not be in the repo, you'll lose points.*

:point_right:
Follow the [lab submission instructions](https://github.com/CS50Dartmouth21FS1/home/blob/main/labs/submit.md), and remember *this is Lab 2*.

To see your Lab submissions, as we see them, visit [classroom.github.com](https://classroom.github.com) and click on your specific Lab 2 repository, and see the files and commit history as GitHub has them.

----------------------------------------------------------------

## chill.c

:point_right:
Write a C program to calculate "wind chill" based on the current temperature and wind speed.
The standard formula for this calculation is:

```Matlab
    Wind Chill = 35.74 + 0.6215T - 35.75(V^0.16) + 0.4275T(V^0.16)
```

where `T` is the temperature in degrees Fahrenheit (when less than 50 and greater than -99) and `V` is the wind velocity in miles per hour.
The `^` character denotes exponentiation.
Note that the above formula is *not* written in C programming language syntax.

### Input:
{:.no_toc}

The user may run your program with no arguments, one argument, or two arguments as explained below.
Chill does not read from stdin or files.

### Output (no arguments):
{:.no_toc}

If the user provides no arguments to your program, it should print out a table of temperatures (from -10 to +40 by 10's) and and wind speeds (from 5 to 15 by 5's).
Your output should look similar to the following, with nice columns and titles:

```bash
$ ./chill
  Temp   Wind  Chill
 -----  -----  -----
 -10.0    5.0  -22.3
 -10.0   10.0  -28.3
 -10.0   15.0  -32.2

   0.0    5.0  -10.5
   0.0   10.0  -15.9
   0.0   15.0  -19.4

  10.0    5.0    1.2
  10.0   10.0   -3.5
  10.0   15.0   -6.6

  20.0    5.0   13.0
  20.0   10.0    8.9
  20.0   15.0    6.2

  30.0    5.0   24.7
  30.0   10.0   21.2
  30.0   15.0   19.0

  40.0    5.0   36.5
  40.0   10.0   33.6
  40.0   15.0   31.8
```

Notice that we print everything in the format "x.y", with exactly one decimal place.

> This format is most important when the user specifies temperature or windspeed, because the user may specify a non-integral value and it may be misleading to print it as an integer.

### Output (one argument):
{:.no_toc}

If the user provides one argument, it refers to a temperature (expressed as a floating-point number).
You may assume (without checking) the argument is a valid floating-point number.
If that temperature is less than 50 and greater than -99, it is acceptable; `chill` then prints a table of wind speeds (from 5 to 15 by 5's) and the calculated wind chills *for that temperature only*.
Your program's output for one argument should look like this:

```bash
$ ./chill 32
  Temp   Wind  Chill
 -----  -----  -----
  32.0    5.0   27.1
  32.0   10.0   23.7
  32.0   15.0   21.6
```

### Output (two arguments):
{:.no_toc}

If the user provides two arguments, they refer to be temperature and velocity, respectively (expressed as floating-point numbers).
You may assume (without checking) the arguments are valid floating-point numbers.
The temperature must be less than 50 and greater than -99.
The velocity must be greater than or equal to 0.5.

If the arguments are acceptable, then your program should calculate and print the wind chill for *that temperature and velocity only*.

Your program's output for two arguments should look like this:

```bash
$ ./chill 32.5 10
  Temp   Wind  Chill
 -----  -----  -----
  32.5   10.0   24.3
```

If either argument is out of range, your program should issue a message and exit.
Here's an example:

```bash
$ ./chill 55
./chill: Temperature must be less than or equal to 50.0 degrees Fahrenheit
$ ./chill 10 0
./chill: Wind velocity must be greater than or equal to 0.5 MPH
```

### Output (more than two arguments):
{:.no_toc}

print a "usage" line and exit with error status.

### Exit:
{:.no_toc}

If the program terminates normally, it exits with a return code  of `0`.
Otherwise, it terminates with a non-zero return code.

### Compiling:
{:.no_toc}

You will likely need the math library.
To use it, add `#include <math.h>` to your `chill.c` file, and add `-lm` to your `mygcc` command.
(That is "dash ell emm", which is short for "library math".)

```bash
mygcc chill.c -lm -o chill
```

### Testing:
{:.no_toc}

:point_right:
Write a simple bash script `testing.sh` that will execute a sequence of commands that demonstrate (a) that your solution works and (b) that you have thoughfully designed test cases that address both normal and erroneous cases.
If you then run it like this:

```bash
$ bash -v testing.sh >& testing.out
```

> The `>&` notation means to send both stdout and stderr to the following filename; it is shorthand for `> testing.out 2>&1` and much more readable!

Here bash (with `-v`) will print each command as it runs, and save the stdout and stderr as well... so we can see what your test does.
For example, the following `testing.sh` contents contain the above example tests.
I include comments to help the reader know the purpose of each test.

```
# zero arguments                                                                    
./chill
# one argument                                                                      
./chill 32
# two arguments                                                                     
./chill 32.5 10
# temp out of range                                                                 
./chill 55
# wind out of range                                                                 
./chill 10 0
```

Your testing should go beyond this example.


### What to submit:
{:.no_toc}

We recommend that you commit the `chill` directory when you have it working, because it is logically separate from commits you may later (or earlier) have done on the other parts of this assignment.
Use a meaningful commit message.

Your directory should contain exactly these files, plus an *optional* `README.md`:

```
chill
├── .gitignore
├── chill.c
├── testing.out
└── testing.sh
```

Note the `.gitignore` file, which causes git to ignore the binary file `chill` (binary files should not be committed!).

-----------------------------------------------------------

## words.c

:point_right:
Write a C program called `words` that breaks its input into a series of words, one per line.
It may take input from stdin, or from files whose names are listed as arguments.

### Usage:
{:.no_toc}

```
words [filename]...
```

### Input:
{:.no_toc}

When no filenames are given on the command line, `words` reads from stdin.

When one or more filenames are given on the command line, `words` reads from each file in sequence.

If the special filename `-` is given as one of the filenames, the stdin is read at that point in the sequence.

### Output:
{:.no_toc}

In any case, the stdout should consist of a sequence of lines, with exactly one word on each output line (i.e., each output line contains exactly one word and no other characters).
**A *word* is a maximal sequence of one or more letters, bounded by the beginning of file, end of file, or any non-letter character.**

Any error messages are written to stderr.
In this program, the most likely error would be failure to open a file named as a command-line argument.

### Exit:
{:.no_toc}

If the program terminates normally, it exits with a return code  of `0`.
Otherwise, it terminates with a non-zero return code.

### Hints:
{:.no_toc}

* Although you may be tempted to think of the input as a sequence of lines, it may be helpful to think of it as a sequence of characters.
* Note it is possible for the output to be empty, if there are no words in any of the input files.
* Check out the [ctype](#ctype) functions, below.
* Consider a function that processes a file, given a `FILE *` as parameter.
* Remember that `stdin` is just a `FILE *` and can be used anywhere a `FILE *` might be used for reading.
* Remember that stdin is not always attached to the keyboard - the input of `words` may be from a pipe or a file (e.g., `./words < thesis.txt`).

### Testing:
{:.no_toc}

:point_right:
Write a simple bash script `testing.sh` that will execute a sequence of commands that demonstrate (a) that your solution works and (b) that you have thoughfully designed test cases that address both normal and erroneous cases.
If you then run it like this:

```bash
$ bash -v testing.sh >& testing.out
```

Here bash (with `-v`) will print each command as it runs, and save the stdout and stderr as well... so we can see what your test does.


### What to submit:
{:.no_toc}

We recommend that you commit the `words` directory when you have it working, because it is logically separate from commits you may later (or earlier) have done on the other parts of this assignment.
Use a meaningful commit message.

Your directory should contain these files:

```
words
├── .gitignore
├── testing.out
├── testing.sh
└── words.c
```
*plus* any input files you need for your testing script, plus an *optional* `README.md`.

Note the `.gitignore` file, which causes git to ignore the binary file `words ` (binary files should not be committed!).


## histo.c

:point_right:
Write a program that reads a series of positive integers from stdin, and prints out a histogram.
There should be 16 bins in your histogram.
The catch? You don't know in advance the *range* of input values; assume the integers range from 0 to some unknown positive maximum.
Thus, you will need to dynamically scale the bin size for your histogram.
An example is below.

### Usage:
{:.no_toc}

There are no command-line arguments.

### Requirements:
{:.no_toc}

You must begin with bin size 1, and double it as needed so all positive integers observed on input fit within the histogram.

You must have 16 bins.
The number '16' should appear only *once* in your code.

### Input:
{:.no_toc}

Input is read from stdin, whether from the keyboard, redirected from a file, or piped in from another command.
Assume the input contains only integers, separated by white space (space, tab, newline).
Assume the smallest integer is zero; ignore any negative integers.
(These assumptions make it easy to use `scanf` for your input.)

As always, any other assumptions you make should be documented in a `README.md` file in this directory.

### Output:
{:.no_toc}

See examples below.

### Exit:
{:.no_toc}

This program has no arguments and does not check its input for errors, so it should always exit with zero status.

### Examples:
{:.no_toc}

Here we compile and run the program, and type a set of numbers (spread over three lines, but it doesn't matter as long as I put space or newline between numbers), ending with `ctrl-D` on the beginning of a line.
(That sends EOF to the program.)  It then printed a histogram, nicely labeling each line with the range of values assigned to that bin, and printing the count of values that fell into that bin.

```
$ mygcc histo.c -o histo
$ ./histo
16 bins of size 1 for range [0,16)
3 -4 5 1 7 0
8 0 15 12 3 5
3 3 3 3 3
^D
[ 0: 0] **
[ 1: 1] *
[ 2: 2]
[ 3: 3] *******
[ 4: 4]
[ 5: 5] **
[ 6: 6]
[ 7: 7] *
[ 8: 8] *
[ 9: 9]
[10:10]
[11:11]
[12:12] *
[13:13]
[14:14]
[15:15] *
$
```

Now watch what happens if I input a number outside the original range of [0,16).

```
$ ./histo
16 bins of size 1 for range [0,16)
3 -4 5 1 7 0
8 0 15 12 3 5
18
16 bins of size 2 for range [0,32)
19 20 30 7 12
50
16 bins of size 4 for range [0,64)
34
32
19
44
^D
[ 0: 3] *****
[ 4: 7] ****
[ 8:11] *
[12:15] ***
[16:19] ***
[20:23] *
[24:27]
[28:31] *
[32:35] **
[36:39]
[40:43]
[44:47] *
[48:51] *
[52:55]
[56:59]
[60:63]
$
```

Each time it sees a number outside the current range, it doubles the range and doubles the size of each bin.
(Notice also the [low:high] labels in the histogram; this notation includes both *low* and *high* and everything in between.) It might have to repeat the doubling if I put in a number well past the current bin size:

```
$ ./histo
16 bins of size 1 for range [0,16)
150
16 bins of size 2 for range [0,32)
16 bins of size 4 for range [0,64)
16 bins of size 8 for range [0,128)
16 bins of size 16 for range [0,256)
^D
[  0: 15]
[ 16: 31]
[ 32: 47]
[ 48: 63]
[ 64: 79]
[ 80: 95]
[ 96:111]
[112:127]
[128:143]
[144:159] *
[160:175]
[176:191]
[192:207]
[208:223]
[224:239]
[240:255]
$
```

Here's an example using bash syntax to generate a list of numbers, and piping the output to `histo`:

```
$ echo {1..16} 150 | ./histo
16 bins of size 1 for range [0,16)
16 bins of size 2 for range [0,32)
16 bins of size 4 for range [0,64)
16 bins of size 8 for range [0,128)
16 bins of size 16 for range [0,256)
[  0: 15] ***************
[ 16: 31] *
[ 32: 47]
[ 48: 63]
[ 64: 79]
[ 80: 95]
[ 96:111]
[112:127]
[128:143]
[144:159] *
[160:175]
[176:191]
[192:207]
[208:223]
[224:239]
[240:255]
$
```

Although we scale the bin size, I'm not asking you to scale the bin count, which is fixed to be 16, or to worry about a bin with a huge count - it's ok if the stars march off the screen.

I took some pains to format the [low:high] range indicators for each row, using a fixed-width field just wide enough to hold the biggest number.
It's a nice touch (read `man printf` for some clues) but it's ok if you make a simpler assumption (e.g., always use 6-digit field width).

### Representing a histogram:
{:.no_toc}

You will need an array of 16 bins to represent the number of integers observed in each bin.
You'll need to keep track of the bin size and the range of the histogram.
If you observe a value outside the range, you should double the bin size and range - but first you need to compress the current 16 bins into the first 8 bins.
You'll likely need one loop to compute the new values for the lower half of the bins (each bin receiving the sum of two bins' counts), and then another to assign the new value (0) to the upper half of the bins.

(Again: the number '16' may only occur *once* in your code; scattering hard-coded numbers around your code is bad style.)

Notice that the number of bins, bin size, and histogram range are all powers of 2.

### Testing:
{:.no_toc}

:point_right:
Write a simple bash script `testing.sh` that will execute a sequence of commands that demonstrate (a) that your solution works and (b) that you have thoughfully designed test cases that address both normal and erroneous cases.
If you then run it like this:

```bash
$ bash -v testing.sh >& testing.out
```

Here bash (with `-v`) will print each command as it runs, and save the stdout and stderr as well... so we can see what your test does.

Unlike some of my examples above, *it must not expect keyboard input.*
The stdin should come from input files or pipes.


### What to submit:
{:.no_toc}

We recommend that you commit the `histo` directory when you have it working, because it is logically separate from commits you may later (or earlier) have done on the other parts of this assignment.
Use a meaningful commit message.

Your directory should contain these files:

```
histo
├── .gitignore
├── histo.c
├── testing.out
└── testing.sh
```
*plus* any input files you need for your testing script, plus an *optional* `README.md`.

Note the `.gitignore` file, which causes git to ignore the binary file `histo` (binary files should not be committed!).


---------------------------------------------------------------

## ctype

The `ctype.h` header file defines several useful functions, listed below.
For information about any of these, check its man page; e.g., `man isdigit`.

```c
   int   isalnum(int);
   int   isalpha(int);
   int   isascii(int);
   int   isblank(int);
   int   iscntrl(int);
   int   isdigit(int);
   int   isgraph(int);
   int   islower(int);
   int   isprint(int);
   int   ispunct(int);
   int   isspace(int);
   int   isupper(int);
   int   isxdigit(int);
   int   toascii(int);
   int   tolower(int);
   int   toupper(int);
```
