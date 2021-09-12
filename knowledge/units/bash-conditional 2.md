In this unit, we discuss shell programming using bash.
The main goal is to write your own scripts.
But what are *scripts?*

In this unit we write simple interactive scripts, including sequences and conditional expressions.

> I replaced my shell prompt with `$` for readability.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=0552d103-59ba-4de6-836f-acfc01631cd8)**

## Interactive mode and shell scripts

The shell can be used in two different ways:

-   *interactive mode*, which allows you to enter more than one command interactively to the shell; we have been doing this already.
-   *shell scripts*, in which the shell reads commands a series of commands (or complex programs) from a text file.

The interactive mode is fine for entering a handful of commands but it becomes cumbersome for the user to keep re-entering these commands interactively.
It is better to store the commands in a text file called a shell script, or *script* for short, and execute the script when needed.
In this way, the script is preserved so you and other users can use it again.

In addition to calling Unix commands (e.g., `grep`, `cd`, `rm`) shell scripts can also invoke compiled programs (e.g., C programs) and other shell scripts.
Shell programming also includes control-flow commands to test conditions (`if...then`) or to do a task repeatedly (`for...do`).
These control structure commands found in many other languages (such as C, or other scripting languages like Python) allow the programmer to quickly write fairly sophisticated shell programs to do a number of different tasks.

Like Python, and unlike C or Java, shell scripts are not *compiled*; rather, they are *interpreted* and executed by the shell itself.

Shell scripts are used for many reasons -- building and configuring systems or environments, prototyping code, or an array of repetitive tasks that programmers do.
Shell programming is mainly built on the Unix shell commands and utilities; reuse of these existing programs enables programmers to simply build new programs to tackle fairly complex jobs.


## Separating groups of commands using ';'

Let's start to build up our knowledge of how scripts work by first looking at some basic operations of the shell.
The Unix shell allows for the unconditional execution of commands and allows for related commands to be kept adjacent as a command sequence using the semicolon character as shown below:

```bash
$ echo Directory listing; date; ls
Directory listing
Sun Mar 28 16:02:41 EDT 2021
Makefile  game    game.o     test2.txt  test4.txt  test6.txt  test8.txt
README    game.c  test1.txt  test3.txt  test5.txt  test7.txt  test9.txt
$ 
```

## Exit status - who cares?

When using the shell interactively it is often clear when we have made a mistake - the shell warns about incorrect syntax, and complains about invalid switches or missing files.
These warnings and complaints can come from the shell's parser and from the program being run (for example, from `ls`).

Error messages provide visual clues that something is wrong, allowing us to adjust the command to get it right.

Commands also inform the shell explicitly whether the command has terminated successfully or unsuccessfully due to some error.
Commands do this by returning an *exit status*, which is represented as an integer value made available to the shell and other commands, programs, and scripts.

The shell understands an exit status of `0` to indicate successful execution, and any other value (always positive) to indicate failure of some sort.

The shell environment variable `$?` is set to the exit status of the most-recent command when that command exits.

```bash
$ echo April Fool
April Fool
$ echo $?
0
$ ls April Fool
ls: cannot access 'April': No such file or directory
ls: cannot access 'Fool': No such file or directory
$ echo $?
2
$ 
```


## Conditional sequences - basic constructs

Why do we need to use the exit status?

Often we want to execute a command based on the success or failure of an earlier command.
For example, we may only wish to remove files if we are in the correct directory, or perhaps we want to be careful to only append info to a file if we know it already exists.

The shell provides both conjunction (and) and disjunction (or) based on previous commands.
These are useful constructs for writing decision-making scripts.
Take a look at the example below in which we make three directories, then try to remove the first:

```bash
$ mkdir labs && mkdir labs/lab1 labs/labs2
$ rmdir labs || echo whoops!
rmdir: failed to remove 'labs': Directory not empty
whoops!
$ 
```

In the first example, `&&` (without any spaces) specifies that the second command should be only executed if the first command succeeds (with an exit status of `0`); that is, we only make the subdirectories if we can make the top directory.

In the second example, (`||`) (without any spaces) requests that the second command is only executed if the first command failed (with an exit status other than `0`).

## Conditional execution using if, then, else

There are many situations when we need to execute commands based on the outcome of an earlier command.

```bash
if command0; then
	command1
	command2
fi
```

Here `command1` and `command2` will be executed if and only if `command0` returns a successful or true value (i.e., its exit status is `0`).

> The fact that `0` means true is confusing for many people!
> (In many high-level languages - like C - zero means false and non-zero means true; technology isn't always consistent.)
> The reason Unix uses `0` for success is that there is only one `0`, but there are many non-zero numbers; thus, `0` implies 'all is well' whereas non-zero implies 'something went wrong', and the specific non-zero value can convey information about *what* went wrong.

Similarly, we may have commands to execute if the conditional fails.

```bash
if command0; then
	command1
	command2
else
	command3
	command4
fi
```

Here `command3` and `command4` will be executed if and only if `command0` fails.

And we can chain if statements, with "else if":

```bash
if command0; then
	command1
	command2
elif
	command3
	command4
else
	command5
fi
```


### First Interactive Shell Program

Entering interactive scripts - that is, a tiny sequence of commands, typed at the keyboard in an interactive shell - is an easy way to get the sense of a new scripting language or to try out a set of commands.
During an interactive session the shell simply allows you to enter a 'one-command' interactive program at the command line and then executes it.

```bash
$ if cp README.md README.back; then
> echo $? copy succeeded!
> else
> echo $? copy failed!
> fi
0 copy succeeded!
$ 
```

The `>` character is the *secondary prompt*, issued by the shell indicating that more input is expected.

The exit status of the `cp` command is used by the shell to decide whether to execute the `then` clause or the `else` clause.
Just for yucks, I had `echo` show us the exit status `$?`; the above example confirms that `0` status means 'true' and triggered the `then` clause.

We can invert the conditional test by preceding it with `!`, as in many programming languages:

```bash
$ if ! cp README.md README.back; then
> echo $? copy failed!
> fi
cp: overwrite 'README.back'? n
$ 
```

Recall that we've set `cp` to `cp -i` so it will check before overwriting the existing file `README.back` (leftover from the previous example).

> Astute readers might note that I did not quote or escape the `!` in the echo commands.
I've noticed that the `!` is not special if it comes last, which is handy for writing interjections!

The `command0` can actually be a sequence or pipeline.
The exit status of the last command is used to determine the conditional outcome.

```bash
$ if mkdir backup && cp README.md backup/README.md
> then
>    echo backup success
> else
>    echo backup failed
> fi
backup success
$ 
```

In the above example, `then` was on the next line instead of at the end of the `if` line.
That's a stylistic choice; if you want it on the `if` line you simply need to put a semicolon (`;`) after the `if` condition and before the word `then`, as seen in the earlier examples.

### The test, aka [ ] command

The `command0` providing the exit status need not be an external command.
We can test for several conditions using the built-in `test` or (interchangeably) the `[ ]` command.
We use both below but we recommend you use the `[ ]` version of the test command because (a) it is more readable and (b) it's more commonly used.
Suppose I want to backup `README.md` only if it exists; the `-f` switch tests whether the following filename names an existing file.

```bash
$ if test -f README.md; then
>    mkdir backup && cp README.md backup/README.md || echo copy failed
> fi
cp: overwrite 'backup/README.md'? y
$ 
```

Rewritten with `[ ]`,

```bash
$ if [ -f README.md ]; then
>    mkdir backup && cp README.md backup/README.md || echo copy failed
> fi
cp: overwrite 'backup/README.md'? y
$ 
```

Note: it's important that you leave spaces around the brackets or you will get syntax errors.
That's because `[` is actually the name of a command (an alias for `test`) and so, like other commands, it must be separated from its arguments.
Similarly, `]` is an argument to that command, and it must be a separate argument.
In short: `[ ]` are not bash syntax, they're a command.

There are other options that can be used with the `[ ]` command.

```
   Option       Meaning
     -e         does the file exist?
     -d         does the directory exist?
     -f         does the file exist and is it an ordinary file (not a directory)?
     -r         does the file exist and is it readable?
     -s         does the file exist and have a size greater than 0 bytes
     -w         does the file exist and is it writeable?
     -x         does the file exist and is it executable?
```

To learn even more about the `test` command:  `man test`,
or see the relevant section of the [Bash reference manual](https://www.gnu.org/software/bash/manual/bash.html#Bash-Conditional-Expressions).

## Do not use [test]

We've introduced you to `test` and its alternate `[ ]` because they appear very commonly in shell scripts... but when using `bash`, we strongly encourage you to use the bash syntax `[[ ]]` for all such conditionals.

First of all, `[[ ]]` is actually syntax interpreted by the shell, not a command, so it is less prone to strange errors;
second, it supports a broader range of conditional expressions.

Bash also supports another syntax: `(( ))`, for conditional expressions involving arithmetic.
We'll see some examples in another unit, soon.

Thus, our last example should be written

```bash
$ if [[ -f README.md ]]; then
>     mkdir backup && cp README.md backup/README.md || echo copy failed
> fi
cp: overwrite 'backup/README.md'? n
$ 
```

Bottom line: use `[[ ]]` unless you need `(( ))` for arithmetic, and always avoid `[ ]`.

### TL;DR here's why

Let's explore each of these forms with a simple example.

```bash
$ five=5
$ six=6
$ ten=10
$ echo $five $six $ten
5 6 10
$ if [ $five > $six ]; then echo greater; else echo smaller; fi
greater
$ if [[ $five > $six ]]; then echo greater; else echo smaller; fi
smaller
$ if (( $five > $six )); then echo greater; else echo smaller; fi
smaller
$ ls
6
$ 
```

Interesting!
The `[` command always got it wrong... *and it created a file named `6`!*

That's because the `>` was left to the shell to interpret, and it interpreted it as "redirect output to the file named in the next word", which (after variable substitution) happened to be `6`.

The other two commands, `[[` and `((`, are built-in bash syntax and thus bash interprets the expression differently.
In those cases, `>` is interpreted as "alphabetically greater than".
In our example, that led to the outcome we expected.

But look at this - a similar example, changing five to ten. 

```bash
$ if [[ $ten > $six ]]; then echo greater; else echo smaller; fi
smaller
$ if (( $ten > $six )); then echo greater; else echo smaller; fi
greater
$ if [[ $ten -gt $six ]]; then echo greater; else echo smaller; fi
greater
$ 
```

In an alphabetic comparison, `10` is less than `6`, but of course it is numerically greater.

The `(( ))` syntax does arithmetic, and got the right answer.

The `[[ ]]` syntax is more common and (I think) more portable; here we can use `-gt` for arithmetic comparison and `>` for alphabetic comparison.

**Again:**
Use `[[ ]]` for all your conditionals, unless you need to do arithmetic within the conditional expression, in which case use `(( ))`.

**Reference:**
see the relevant section of the [Bash reference manual](https://www.gnu.org/software/bash/manual/bash.html#Bash-Conditional-Expressions).
