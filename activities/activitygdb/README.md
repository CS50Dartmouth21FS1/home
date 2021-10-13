> “Debugging is twice as hard as writing the code in the first place.
> Therefore, if you write the code as cleverly as possible, you are, by definition, not smart enough to debug it.” - Brian Kernighan

As we turn our attention towards larger, more complex C programs we stress the importance of good style, good documentation, and strong testing.
The goal is to avoid bugs through careful design and good style - and to discover what bugs remain through strong testing.

Once you discover the existence of a bug, how do you track it down so you know *why* the program is misbehaving and then how to fix it?

**We strongly recommend learning `gdb` for debugging C programs**.
It takes a bit of practice, but its use will save you *lots* of time in CS50 and subsequent courses.

## The GNU Debugger (gdb)

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ca07df68-be42-4fcc-b521-ad0d015fd9bc)**

See our [gdb resources](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/logistics/systems.md#gdb) for manuals and tutorial documents.

**Note:** before using `gdb`, ensure you compile all C source files with the `-ggdb` flag - our standard `.bashrc` file defines `mygcc` with this flag, and your `Makefile` should include this flag in its definition of `CFLAGS`.
This flag ensures that useful metadata is packaged with your executable at compile time that `gdb` needs to help you debug your programs.

So for now, just compile using `mygcc`:

```
mygcc bugsort.c -o bugsort
```

The gdb debugger is invoked with the shell command `gdb`; it then prints its own prompt and accepts its own wide range of commands. Once started, it reads commands from the terminal until you tell it to exit with the gdb command `quit`.
You can always get online help while using `gdb` with the command `help`.

```
$ gdb
GNU gdb (Ubuntu 8.1.1-0ubuntu1) 8.1.1
...
(gdb) help
List of classes of commands:

aliases -- Aliases of other commands
breakpoints -- Making program stop at certain points
data -- Examining data
files -- Specifying and examining files
internals -- Maintenance commands
obscure -- Obscure features
running -- Running the program
stack -- Examining the stack
status -- Status inquiries
support -- Support facilities
tracepoints -- Tracing of program execution without stopping the program
user-defined -- User-defined commands

Type "help" followed by a class name for a list of commands in that class.
Type "help all" for the list of all commands.
Type "help" followed by command name for full documentation.
Type "apropos word" to search for commands related to "word".
Command name abbreviations are allowed if unambiguous.
(gdb) 
```

You can run `gdb` with no arguments or options; but the most usual way to start GDB is with one argument, specifying an executable program as the argument:

```bash
$ gdb program
```

## Sample `gdb` session

In the following examples we will use a lot of the basic `gdb` commands - `break`, `run`, `next`, `step`, `continue`, `display`, `print`, and `frame` (read about [stack frames](http://sourceware.org/gdb/current/onlinedocs/gdb/Frames.html); this is an important concept in C and very useful for debugging and poking around in your code and looking at variables).

I strongly recommend that you go through the sequence of steps below and use these debugging commands. Don't worry, you can't break anything.

Just like the shell commands you'll only need a subset of the the complete set of `gdb` commands to become an effective debugger.

We will be working with [bugsort.c ](https://github.com/CS50Dartmouth21FS1/examples/blob/main/bugsort.c)  in our examples directory on plank..

The program is simple: it reads ten integers from the stdin, and inserts each into an array of integers such that the array is in sorted order. It then prints them out, separated by spaces.
Easy, right?

```
$ echo 1 2 3 4 5 6 7 8 9 0 | ./bugsort
0 9 9 9 9 9 9 9 9 9 
```

Um, I guess not.

Let's try running our program in `gdb`.
When `gdb` starts up it prints out a bunch of information about its version and license, then drops into the `gdb` "shell."

```
$ gdb bugsort
GNU gdb (Ubuntu 8.1.1-0ubuntu1) 8.1.1
...
Reading symbols from bugsort...done.
(gdb)
```

Notice that last line about "reading symbols"; `gdb` is reading special debug-related information the compiler produced, about all the "symbols" in the program.

> A *symbol* is a function name, variable name, data type name, etc.
> This information may be stored inside the executable file (here, `bugsort`), or (as on MacOS) in an adjacent folder (`bugsort.dSYM`).
> The compiler saves this information because our alias `mygcc` includes the `-ggdb` argument.

### breakpoints

One of a debugger's most powerful features is the ability to set "breakpoints" in our code; when we run our program and the debugger encounters a breakpoint, the execution of the program stops at that point.
Let's set a few breakpoints:

```
(gdb) break main
Breakpoint 1 at 0x773: file bugsort.c, line 19.
(gdb) list 26
21	  int sorted[numSlots];   // the array of items
22	  
23	  /* fill the array with numbers */
24	  for (int n = 0; n < numSlots; n++) {
25	    int item;     // a new item
26	    scanf("%d", &item);   // read a new item
27	    for (int i = n; i > 0; i--) {
28	      if (sorted[i] > item) {
29	        sorted[i+1] = sorted[i]; // bump it up to make room
30	      } else {
(gdb) 
31	        sorted[i] = item; // drop the new item here
32	      }
33	    }
34	  }
35	  
36	  /* print the numbers */
37	  for (int n = 0; n < numSlots; n++) {
38	    printf("%d ", sorted[n]);
39	  }
40	  putchar('\n');
(gdb) break 27
Breakpoint 2 at 0x817: file bugsort.c, line 27.
(gdb) break 37
Breakpoint 3 at 0x878: file bugsort.c, line 37.
(gdb) 
```

Notice that we can list the code around a line number by specifying that line number.
Notice further that you can just hit "enter" at the gdb commandline to mean "do the last command again", or in the case of `list`, "list some more".

Notice that we can set breakpoints by identifying the name of a function (e.g., `main`, `myfunc`, etc.), or by specifying a particular line in our source code (e.g., lines 27 and 37).

> When you are debugging programs with multiple files you can also set breakpoints in different files by specifying the file as well as the function name/line of code where you'd like to enable a breakpoint.

If you want to see the breakpoints you've currently created, run `info break` (as shown above).

```
(gdb) info break
Num     Type           Disp Enb Address            What
1       breakpoint     keep y   0x0000000000000773 in main at bugsort.c:19
2       breakpoint     keep y   0x0000000000000817 in main at bugsort.c:27
3       breakpoint     keep y   0x0000000000000878 in main at bugsort.c:37
(gdb) 
```

You can also clear all of your breakpoints (`clear`), clear specific breakpoints (`clear` *function* or `clear` *line*), or even disable specific breakpoints so that you can leave them in place, but temporarily disabled.

```
(gdb) disable 2
(gdb) info break
Num     Type           Disp Enb Address            What
1       breakpoint     keep y   0x0000000000000773 in main at bugsort.c:19
2       breakpoint     keep n   0x0000000000000817 in main at bugsort.c:27
3       breakpoint     keep y   0x0000000000000878 in main at bugsort.c:37
(gdb) 
```

Notice under the `Enb` column the second breakpoint is disabled.

At this point we've started `gdb` and told it about some breakpoints we want set, but we haven't actually started running our program.

Let's run our program now:

```
(gdb) run
Starting program: /thayerfs/home/d29265d/web/Lectures/_examples/bugsort 

Breakpoint 1, main () at bugsort.c:19
19	{
(gdb) 
```

As expected, the debugger started our program running but "paused" the program as soon as it hit the breakpoint that we set at the `main` function.
Once the program has stopped we can "poke around" a bit.

Now let's `step` one line of code at a time; we type `step` and then, for convenience, just hit **Enter** each time to go one more step.

```
(gdb) step
20	  const int numSlots = 10;  // number of slots in array
(gdb) 
21	  int sorted[numSlots];   // the array of items
(gdb) 
24	  for (int n = 0; n < numSlots; n++) {
(gdb) 
26	    scanf("%d", &item);   // read a new item
(gdb) 
__isoc99_scanf (format=0x555555554964 "%d") at isoc99_scanf.c:27
27	isoc99_scanf.c: No such file or directory.
(gdb) 
```

##### Oops! Stepping line by line is nice but `step` command allowed us to walk right down into the icky details of `scanf`!

It is cool that we can "step" into functions but `scanf` does a lot of work that we aren't interested in - and we don't have the source code for it anyway.

If you find yourself deep down in some function that you accidentally stepped into, use the `finish` command to start the program running again until just after the function in the current stack frame returns.

```
(gdb) finish
Run till exit from #0  __isoc99_scanf (format=0x555555554964 "%d")
    at isoc99_scanf.c:27
11 22 33 44 55 66 77 88 99 00
main () at bugsort.c:27
27	    for (int i = n; i > 0; i--) {
Value returned is $1 = 1
(gdb) 
```

Of course, when `scanf` continued it expected me to enter some input.
I proceeded to enter 10 numbers, just as in our prior experiment.
(In this case the input is from the keyboard, and in the prior case it was from a pipeline; either way it is coming via stdin and scanf does not care.)

Now we are back up in the `main` function, at line 27.

`gdb`  conveniently prints the return value for `scanf`, i.e., `Value returned is $1 = 1` (because `scanf` successfully read 1 item matching the pattern `%d`).
I can examine what number was read by printing the variable value:

```
(gdb) print item
$2 = 11
```

We're about to enter inner `for` loop.
Let's take one step.

```
(gdb) step
24	  for (int n = 0; n < numSlots; n++) {
```

Huh?  we never entered the inner `for` loop - we came right back around and are about to re-execute line 24.
(Think about why.)

To avoid stepping *into* functions we can use the alternative `gdb` command called `next` which is similar to `step` in that it executes one line of code and then pauses at the next line of code, however `next` will step *over* functions so that we don't end up deep down in some code that isn't relevant to us (i.e., deep inside of the details of `scanf`); let's try that now:

```
(gdb) next
26	    scanf("%d", &item);   // read a new item
(gdb) next
27	    for (int i = n; i > 0; i--) {
(gdb) print item
$3 = 22
(gdb) print n
$4 = 1
(gdb) 
```

Back at that breakpoint, and this time `item=22` and `n=1`.

Moving on,

```
(gdb) next
28	      if (sorted[i] > item) {
(gdb) 
31	        sorted[i] = item; // drop the new item here
(gdb) 
27	    for (int i = n; i > 0; i--) {
(gdb) 
24	  for (int n = 0; n < numSlots; n++) {
(gdb) 
26	    scanf("%d", &item);   // read a new item
(gdb) print sorted[0]
$5 = 0
(gdb) print sorted[1]
$6 = 22
(gdb) 
```

Aha! This time we went into the inner loop and dropped in our item.
We then came back around to the top of the main loop.
As you can see, I printed contents of two elements of the array, too.

We can print the memory address of these variables:

```
(gdb) print &n
$7 = (int *) 0x7fffffffdde0
(gdb) print &sorted
$8 = (int (*)[10]) 0x7fffffffdda0
(gdb) 
```

Pretty cool, right?
Notice that `gdb` is nice enough to also give us information about the *type* of the thing that we are looking at!

If we `step` a bit further, and into `scanf`, I can show you the `backtrace` command:

```
(gdb) step
25	    scanf("%d", &item);		// read a new item
(gdb) step
__isoc99_scanf (format=0x555555554964 "%d") at isoc99_scanf.c:27
27	isoc99_scanf.c: No such file or directory.
(gdb) backtrace
#0  __isoc99_scanf (format=0x555555554964 "%d") at isoc99_scanf.c:27
#1  0x0000555555554817 in main () at bugsort.c:26
(gdb) 
```

Which shows the function-call stack, from inner to outer.  

Above we are inside `__isoc99_scanf` (aka `scanf`) and that was called from `main`.

Let's finish `scanf`:

```
(gdb) finish
Run till exit from #0  __isoc99_scanf (format=0x555555554964 "%d")
    at isoc99_scanf.c:27
main () at bugsort.c:27
27	    for (int i = n; i > 0; i--) {
Value returned is $9 = 1
(gdb) print item
$10 = 33
(gdb) 
```

Notice I did not need to type any input because scanf is still chewing on that input I provided the first time it asked me for input.

OK, I'm getting tired of stepping.
Rather than stepping line by line, I want to start the program running again (at least until it hits the breakpoint again) so that I can speed up the process getting back to the code where I can enter a password and verify the changes.
To do this I can simply use the `continue` command which will continue the execution of the program until it is stopped again for some reason.
First, I'm going to re-enable that breakpoint I disabled earlier.

```
(gdb) enable 2
(gdb) continue
Continuing.

Breakpoint 2, main () at bugsort.c:27
27	    for (int i = n; i > 0; i--) {
(gdb) 
```

It ran a bit further then hit that breakpoint.
Let's automate things a little better, by providing some commands that should be run on certain breakpoints:

```
(gdb) commands 2
Type commands for breakpoint(s) 2, one per line.
End with a line saying just "end".
>print n
>print item
>end
(gdb) continue
Continuing.

Breakpoint 2, main () at bugsort.c:27
27	    for (int i = n; i > 0; i--) {
$11 = 4
$12 = 55
(gdb) 
Continuing.

Breakpoint 2, main () at bugsort.c:27
27	    for (int i = n; i > 0; i--) {
$13 = 5
$14 = 66
(gdb) 
Continuing.

Breakpoint 2, main () at bugsort.c:27
27	    for (int i = n; i > 0; i--) {
$15 = 6
$16 = 77
(gdb) 
Continuing.

Breakpoint 2, main () at bugsort.c:27
27	    for (int i = n; i > 0; i--) {
$17 = 7
$18 = 88
(gdb) 
Continuing.

Breakpoint 2, main () at bugsort.c:27
27	    for (int i = n; i > 0; i--) {
$19 = 8
$20 = 99
(gdb) 
Continuing.

Breakpoint 2, main () at bugsort.c:27
27	    for (int i = n; i > 0; i--) {
$21 = 9
$22 = 0
(gdb) 
Continuing.

Breakpoint 3, main () at bugsort.c:37
37	  for (int n = 0; n < numSlots; n++) {
(gdb) 
```

Now I just hit Enter each time, and it took another loop and shows me the values of `n` and `item`.  Handy!
The last one broke out of the initial loop and landed me at breakpoint 3, just before the values will be printed.
I can explore a bit more before that loop runs.

```
(gdb) print sorted[0]
$23 = 0
(gdb) print sorted[9]
$24 = 32767
(gdb) continue
Continuing.
0 99 99 99 99 99 99 99 32767 32767 
[Inferior 1 (process 38631) exited normally]
(gdb) 
```

At this point we've seen some useful `gdb` commands and you are now equipped to do some debugging on your own.
Keep poking at the program and see if you can find the errors.

You may find it helpful to store sample input in a file, e.g.,

```
$ examine
$ gdb bugsort
...
Reading symbols from bugsort...done.
(gdb) run < nums
Starting program: /thayerfs/home/d29265d/web/Lectures/_examples/bugsort < nums
0 99 99 99 99 99 99 99 32767 32767 
[Inferior 1 (process 40061) exited normally]
(gdb) 
```

---

---

**Some cool things to note about gdb:**

* Every time you enter a command at the `gdb` "shell" that is successful, the output value is stored in a variable denoted `$N` where `N` increments by 1 for each command that you run.
  You can use those variables at a later point if you want (e.g., `print $3`).

* `gdb` supports auto-completion on function names and variable names! Go ahead and try it out!

* When exploring the stack frames using the `backtrace` or `bt` command,  if you include the `full` qualifier, it also shows the values of the local variables for each stack frame.  Here's an example from a debug session for `bagtest.c`.

  ```bash
  (gdb) bt full
  #0  itemcount (arg=0x7fffffffdd78, item=0x555555758690) at bagtest.c:115
          nitems = 0x7fffffffdd78
  #1  0x00005555555551dd in bag_iterate (bag=0x555555758670, arg=0x7fffffffdd78,
      itemfunc=0x555555554efb <itemcount>) at bag.c:139
          node = 0x55555575a700
  #2  0x0000555555554cfa in main () at bagtest.c:64
          bag1 = 0x555555758670
          bag2 = 0x0
          name = 0x0
          namecount = 1
          bagcount = 1
  (gdb)
  ```

  

* Lots of extra functionality and settings are available via the `set` command.  For example, if you enter `set print pretty` in `gdb` and then you print a variable that is a struct, `gdb` will print it nicely with indentions and such. Here's an excerpt from a debugging session for `bagtest`:

  ```bash
  (gdb) p bag1
  $8 = (bag_t *) 0x555555758670
  (gdb) p *bag1
  $9 = {head = 0x555555758c00}
  (gdb) set print pretty on
  (gdb) p *bag1
  $10 = {
    head = 0x555555758c00
  }
  (gdb) p bag1->head
  $11 = (struct bagnode *) 0x555555758c00
  (gdb) p *(bag1->head)
  $12 = {
    item = 0x555555758ba0,
    next = 0x555555758b80
  }
  (gdb) p *(bag1->head->next)
  $13 = {
    item = 0x555555758b20,
    next = 0x555555758b00
  }
  (gdb)
  ```

* Also similar to the regular shell, the `gdb` shell allows you to arrow up/down to revisit past commands.

* You can re-run the previous command simply by hitting the Enter (return) key.

* Many of the `gdb` commands have abbreviated forms (e.g., `run` is `r`, `continue` is `c`, `next` is `n`); see the [gdb quick reference guide](http://users.ece.utexas.edu/~adnan/gdb-refcard.pdf) to see other commands that have abbreviated forms.



### Frequently used `gdb` commands

Below are some of the more common `gdb` commands that you will need.
See also this printable [gdb quick reference guide](http://users.ece.utexas.edu/~adnan/gdb-refcard.pdf).

| command                 | purpose                 |
| :---------------------- | :---------------------- |
| `run [arglist]`         |Start your program (with arglist, if specified). |
| `break [file:]function` |Set a breakpoint at function (in file). |
| `commands NN`           |A list of commands to run every time breakpoint #NN is reached. |
| `list [file:]function`  |Type  the  text  of  the  program  in  the  vicinity of where it is presently stopped. |
| `backtrace`             |Backtrace: display the program stack. |
|  | |
| `frame [args]`          |The frame command allows you to move from one stack frame to another, and to print the stack frame you select. args may be either the address of the frame or the stack frame number. Without an argument, frame prints the current stack frame. |
| `print expr`            |Display the value of an expression. |
| `continue`              |Continue running your program (after stopping, e.g. at a breakpoint). |
| `next`                  |Execute next program line (after stopping); step over any function calls  in the line. |
| `step`                  |Execute next program line (after stopping); step into any function  calls  in the line.|
| `help [name]`           |Show information about GDB command name, or general information about using GDB. |
| `quit`                  |Exit from GDB.|
