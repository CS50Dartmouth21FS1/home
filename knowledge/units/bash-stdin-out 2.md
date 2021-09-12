This unit covers the shell standard input, output, and error, and how to redirect them... and how to connect commands into pipelines.

> I replaced my shell prompt with `$` for readability.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=48d21ab8-70e2-4432-bedb-acfb016abeb2)**

## Redirection and pipes

The shell executes all Unix programs with an input file called *standard input* (stdin) and an output file called *standard output* (stdout).
It attaches the keyboard to stdin and the display to stdout.
But you can ask the shell to *redirect* the input and output of programs.
We've already seen an example of output redirection, like the following.

```bash
$ echo hello class > somefile
$ cat somefile
hello class
$ 
```

The output redirection `>` writes the output of `echo` to the file called `somefile`; that is, the 'standard output' of the `echo` process has been directed to the file instead of the default, the display.
If the file does not exist, it is created.
If the file already exists, it is erased before the new content is added.

```bash
$ date > listing
$ ls dotfiles >> listing
$ cat listing
Sun Mar 28 14:21:01 EDT 2021
README.md
bashrc.cs50
emacs
gitconfig
profile.cs50
vimrc
$ 
```

Here, the output redirection `>` writes the output of `date` to the file called `listing`; that is, the 'standard output' of the `date` process has been directed to the file instead of the default, the display.
Note that the `>` operation created a file that did not exist before the output redirection command was executed.
Next, we append a directory listing to the same file; by using the `>>` (double `>`) we tell the shell to *append* to the file rather than overwriting the file.

Note that the `>` or `>>` and their target filenames are *not* arguments to the command - the command simply writes to the standard output ("stdout"), as it always does, but the shell has arranged for stdout to be directed to a file instead of the terminal.

The shell also supports input redirection.
This provides input to a program (rather than the keyboard).
Let's create a file of prime numbers using output redirection.
The input to the `cat` command can come from the standard input (i.e., the keyboard).
We can instruct the shell to redirect the `cat` command's output (stdout) to file named `primes`.

```bash
$ cat > primes
61
53
41
2
3
11
13
18
37
5
19
23
29
31
47
53
59
$ 
```

Input redirection `<` tells the shell to use a file as input to the command rather than the keyboard.
After I typed all those numbers, I typed `^D` (control-d) at the beginning of an empty line, which sent an `EOF` to the `cat` program.
As far as `cat` was concerned, its input from its input file (stdin) was done and so it concluded its work, closed its output file (stdout) and exited.
In the input redirection example below `primes` is used as input to `cat` which sends its standard output to the screen.

```bash
$ cat < primes
61
53
41
2
3
11
13
18
37
5
19
23
29
31
47
53
59
$ 
```

I could redirect both stdin and stdout:

```bash
$ cat < primes > primes2
$ 
```

In all three cases, `cat` simply read from stdin and wrote to stdout, completely unaware of whether its input was a file or the keyboard, or its output was a file or the display.

Many Unix commands (e.g., `cat`, `sort`) allow you to provide input from stdin if you do not specify a file on the command line.

Unix also supports a powerful 'pipe' operator for passing data between commands using the operator `|` (a vertical bar, usually located above the `\` key on your keyboard).
Pipes connect commands that run as separate processes as data becomes available the processes are scheduled.

Pipes are a clever invention indeed, since the need for separate temporary files for sharing data between processes is not required.
Because commands are implemented as processes, a program reading an empty pipe will be "suspended" until there is data or information ready for it to read.
There is no limit to the number of programs or commands in the pipeline.
In our example below there are four programs in the pipeline, all running simultaneously waiting on the input:

```bash
$ sort -n primes | uniq | grep -v 18 | more
2
3
5
11
13
19
23
29
31
37
41
47
53
59
$ 
```

What is the difference between pipes and redirection?
Basically, redirection (`>`,`>>`,`<`) is used to direct the stdout of command to a file, or from a file to the stdin of a command.
Pipes (`|`) are used to redirect the stdout to the stdin of another command.
This operator allows us to 'glue' together programs as 'filters' to process the plain text sent between them (*plain text* between the processes - a nice design decision).
This supports the notion of reuse and allows us to build sophisticated programs quickly and simply.
It's another cool feature of Unix.

Notice three new commands above: `sort`, `uniq`, and `grep`.

* `sort` reads lines from from stdin and outputs the lines in sorted order; here `-n` tells `sort` to use numeric order (rather than alphabetical order);
* `uniq` removes duplicates, printing only one of a run of identical lines;
* `grep` prints lines matching a pattern (more generally, a *regular expression*); here, `-v` inverts this behavior: print lines that *do not* match the pattern.
In this case, the pattern is simply `18` and `grep` does not print that number as it comes through.

And, as we saw last time, `more` pauses the output when it would scroll off the screen.

Note that the original file - `primes` - is not changed by executing the command line above.
Rather, the file is read in by the `sort` command and the data is manipulated as it is processed by each stage of the command pipe line.
Because `sort` and `cat` are happy to read their input data from stdin, or from a file given as an argument, the following pipelines all achieve the same result:

```bash
 sort -n primes         | uniq | grep -v 18 | more
 sort -n < primes       | uniq | grep -v 18 | more
 cat   primes | sort -n | uniq | grep -v 18 | more
 cat < primes | sort -n | uniq | grep -v 18 | more
```

Which do you think would be most efficient?

**Another pipeline**: list the set of user accounts on our Linux box.
(These are mostly special users; regular Dartmouth users login via NetID and are not listed in `/etc/passwd`.)

```bash
 cut -d : -f 1 /etc/passwd | sort > usernames.txt
```

> See `man cut` to understand what the first command does, and how it interprets its five arguments.

**Another example**: what is the most popular shell?
Try each of these in turn:

```bash
 cut -d : -f 7 /etc/passwd
 cut -d : -f 7 /etc/passwd | less
 cut -d : -f 7 /etc/passwd | sort
 cut -d : -f 7 /etc/passwd | sort | uniq -c
 cut -d : -f 7 /etc/passwd | sort | uniq -c | sort -n
 cut -d : -f 7 /etc/passwd | sort | uniq -c | sort -nr
```

## Standard error

As we learned above, every process (a running program) has a *standard input* (abbreviated to "stdin") and a *standard output* ("stdout").
The shell sets stdin to the keyboard by default, but the command line can tell the shell to redirect stdin using `<` or a pipe.
The shell sets stdout to the display by default, but the command line can tell the shell to redirect stdout using `>` or `>>`, or to a pipe.

Each process also has a *standard error* ("stderr"), which most programs use for printing error messages.
The separation of stdout and stderr is important when stdin is redirected to a file or pipe, because normal output can flow into the file or pipe while error messages reach the user on the screen.

Inside the running process these three streams are represented with numeric *file descriptors*:

1. stdin
2. stdout
3. stderr

You can tell the shell to redirect using these numbers; `>` is shorthand for `1>` and `<` is shorthand for `0<`.
You can thus redirect the standard error (file descriptor 2) with the symbol `2>`.
Suppose I was curious about what HTML files exist in the course directories on thayerfs; I can use the `find` command:

```bash
$ find /thayerfs/courses/ -name \*.html > pages
find: '/thayerfs/courses/18winter/engs032': Permission denied
find: '/thayerfs/courses/18winter/engs020': Permission denied
find: '/thayerfs/courses/18winter/engs075': Permission denied
find: '/thayerfs/courses/18winter/engg199.01': Permission denied
find: '/thayerfs/courses/18winter/engs011': Permission denied
find: '/thayerfs/courses/18winter/engg199.02': Permission denied
find: '/thayerfs/courses/18winter/engs033': Permission denied
find: '/thayerfs/courses/18winter/engs105': Permission denied
...
```

I redirected the output of `find` to a file called `pages` - and indeed that file filled with useful information - but `find` printed its error messages (not all shown here!) to the screen so I could still see them.
That's because the shell connected the *standard error* output to my screen.
Suppose I wanted to capture those errors in a file too:

```bash
$ find /thayerfs/courses/ -name \*.html > pages 2> errors
$ 
```
The file `errors` contains the error messages we saw earlier.

In case you're curious, `wc` (word count) will count the number of lines (`-l`) in each file:

```bash
$ wc -l pages errors
  549 pages
   93 errors
  642 total
$ 
```

As another alternative, we could ignore the error output entirely by sending it to a place where all characters go and never return!

```bash
$ find /thayerfs/courses/ -name \*.html > pages 2> /dev/null
$ 
```
The file called `/dev/null` is a special kind of file - it's not a file at all, actually, it's a 'device' that simply discards anything written to it.
(If you read from it, it appears to be an empty file.)

