In this unit we actually create a *shell script* - a shell program saved into an executable file.
We also explore *variables*, including arrays, and built-in variables automatically provided by the shell.

> I replaced my shell prompt with `$ ` for readability.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ac22e589-41a2-4c21-a219-acfc016984f2)**

## First Shell Script

Up until now we have entered scripts interactively into the shell.
It is a pain to have to keep re-entering scripts interactively.
It is better to store the script commands in a text file and then execute the script when we need it.
So how do we do that?

Simple!  Write the commands in a file, and ask `bash` to read commands from the file instead of from the keyboard.

For example, we can put our simple commands into a file:

```bash
$ cat > media.sh
echo "# Images in $(pwd)" > media.md
for img in *.png
 do
    echo >> media.md
    echo "## $img" >> media.md
    echo '![]'"($img)" >> media.md
done
$ 
```

Here I've typed it at the keyboard (ending with ^D), but for more complex scripts, you would of course want to use a text editor.

Indeed, we can go further, and make the file into a command executable at the shell prompt; to do so, you should

1. add a special string `#!/bin/bash` to the first line,
2. make it executable (with `chmod`), and
3. either
 - add it to a directory on our `PATH`, or
 - type its pathname at the commandline.

So, for `media.sh`, it looks like this:

```bash
$ emacs media.sh 
$ chmod +x media.sh
$ ls -l media.sh
-rwxr-xr-x  1 dfk  staff  162 Mar 28 16:51 media.sh*
$ cat media.sh
#!/bin/bash
echo "# Images in $(pwd)" > media.md
for img in *.png
 do
    echo >> media.md
    echo "## $img" >> media.md
    echo '![]'"($img)" >> media.md
done
$ ls
media.sh*		ssh-add-key-1.png	ssh-copy.png
media.sh~		ssh-add-key-2.png	ssh-generation.png
$ ./media.sh 
$ ls
media.md		ssh-add-key-1.png	ssh-generation.png
media.sh*		ssh-add-key-2.png
media.sh~		ssh-copy.png
```

There are a couple of things to note about this example.

First, there is the `#!/bin/bash` line.
What does this mean?
Typically, the `#` in the first column of a file denotes the start of a comment until the end of the line.
Indeed, in this case, this line is treated as a comment by `bash`.
*Unix, however, reads that line when you execute the file and uses it to determine which command should be fed this file;* thus, in effect, Unix will execute ```/bin/bash ./media.sh```.
Then bash reads the file and interprets its commands.
The `#!/bin/bash` must be the first line of the file, exactly like that - no spaces.

Second, there is `chmod +x`,  which sets the 'execute' permission on the file.
(Notice the 'x' characters in the file permissions displayed by `ls`.)  Unix will not execute files that do not have 'execute' permission, and the shell won't even try.

Third, we used the pathname `./media.sh` when treating our script as a command, because `.` is not on our `PATH`.
If `.` were on our `PATH`, we could have typed just `media.sh`.

> It is very tempting to have `.` on your `PATH`, but it is a big security risk.
If you `cd` to a directory with an executable file called, say, `ls` and you don't notice, bad things might happen when you type the command `ls`.
If `.` is on your `PATH` before `/bin` you will run the local command `./ls` instead of the official `/bin/ls`... and the local `ls` may be malicious and do something bad!

Fourth, this script has no comments, and is limited to png files.
Let's look instead at [imagepage.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/main/imagepage.sh).

```bash
#!/bin/bash
#
# image page: build an index page for a set of images.
# Creates index.md to refer to *.{jpg,jpeg,png,gif,tif,tiff,pdf}
#
# David Kotz, March 2021

cat > index.md <<EOF
# Images

EOF

# see https://www.gnu.org/software/bash/manual/html_node/The-Shopt-Builtin.html
shopt -s nullglob

for img in *.{jpg,jpeg,png,gif,tif,tiff,pdf}
do
    echo "![]($img)" >> index.md
    echo >> index.md
done

exit 0
```

It is good practice to identify the program, how its command-line should be used, and a description of what it does (if anything) with stdin and stdout.
And to list the author name(s) and date.

Notice the script returns the exit status `0`, which can be viewed using the `echo $?` command, as discussed earlier.
The return status is typically not checked when scripts are run from the command line.
However, when a script is called by another script the return status is typically checked - so it is important to return a meaningful exit status.

Notice also a new form of redirection, which we used on `cat`.
It is called a "here document".
The syntax `<<EOF` indicates that stdin should be fed from whatever follows, up to a line containing exactly the word `EOF`.
We use it to initialize the header of the index file.

---

**Remember:**
You can find all the example programs in the directory `~cs50-dev/shared/examples/`.
We encourage you to copy examples from that directory, and experiment!
For this example,

```bash
$ cp ~cs50-dev/shared/examples/imagepage.sh .
```

copies the `imagepage.sh` file to your current directory `.`;
`cp` is smart enough to realize that you mean it to create `./imagepage.sh`.


---

## Variables and arrays

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=a2ac8956-441f-4c73-b6c7-acfc016c3330)**

We've already seen that you can declare variables in the shell.
Variables are typically not declared before they are used in scripts.

```bash
$ a=5
$ message="good morning"
$ echo $a
5
$ echo $message
good morning
$ echo ${message}
good morning
$ 
```

> Note that tab-key expansion works on bash variables!  
> type this:
> `echo $mes[tabkey]`
> you should see $message completed !

Above we create two variables (`a` and `message`).
The final command shows the `${varname}` syntax for variable substitution; this is the general form whereas `$varname` is a shorthand that works for simple cases; note that `${message}` is identical to `$message`.  The `{}` are needed to resolve ambiguity, so it's safer to always use them.  For example:

```bash
var=hello
echo $varworld
# Prints nothing because there is no variable 'varbar'
echo ${var}world
# helloworld
```

Like variables, arrays are typically not declared before they are used in scripts.
(Exception: associative arrays; I won't get into that here.)

```bash
[$ colors=(red orange yellow green blue indigo violet)
$ echo $colors
red
$ echo ${colors[1]}
orange
$ echo $colors[4]  # whoops!
red[4]
$ echo ${colors[6]}
violet
$ echo ${colors[7]}

$ 
```

Above we create one array (`colors`).
Notice that `$colors` implicitly substitutes the first element.
The later commands show the `${varname}` syntax for variable substitution; this is the general form whereas `$varname` is a shorthand that works for simple cases; `$colors` is equivalent to `${colors[0]}` (we computer scientists like counting from zero).
When desiring to subscript an array variable, you must use the full syntax, as in `${colors[1]}`.
Finally, note that `${colors[7]}` is empty because it was not defined.

Even cooler, the array can be used in combination with file substitution `$(<filename)` and command substitution `$(command)`:

```bash
$ cat learning-fellow-emails 
Darren.Gu.22@dartmouth.edu
David.F.Kotz@dartmouth.edu
Jackson.R.McGary.23@dartmouth.edu
Jacob.M.Chen.22@dartmouth.edu
Jacob.E.Werzinsky.22@dartmouth.edu
Kelly.B.Westkaemper.22@dartmouth.edu
Rachael.E.Williams.23@dartmouth.edu
Rylee.R.Stone.21@dartmouth.edu
Wending.Wu.23@dartmouth.edu
William.P.Dinauer.23@dartmouth.edu
$ lfs=($(<learning-fellow-emails))
$ echo ${lfs[2]}
Jackson.R.McGary.23@dartmouth.edu
$ juniors=($(grep .22. learning-fellow-emails))
$ echo ${juniors}
Darren.Gu.22@dartmouth.edu
$ echo ${lfs[*]}
Darren.Gu.22@dartmouth.edu David.F.Kotz@dartmouth.edu Jackson.R.McGary.23@dartmouth.edu Jacob.M.Chen.22@dartmouth.edu Jacob.E.Werzinsky.22@dartmouth.edu Kelly.B.Westkaemper.22@dartmouth.edu Rachael.E.Williams.23@dartmouth.edu Rylee.R.Stone.21@dartmouth.edu Wending.Wu.23@dartmouth.edu William.P.Dinauer.23@dartmouth.edu
$ 
```

The last line demonstrates how you can substitute all values of the array, with the `[*]` index.

## The shell's variables

The shell maintains a number of important variables that are useful in writing scripts.
We have come across some of them already.

```
  Variable            Description
     $USER              username of current user
     $HOME              pathname for the home directory of current user
     $PATH              a list of directories to search for commands
     $#                 number of parameters passed to the script
     $0                 name of the shell script
     $1, $2, .. $#      arguments given to the script
     $*                 A list of all the parameters in a single variable.
     $@                 A list of all the parameters in a single variable; always delimited
     $$                 process ID of the shell script when running
```

The variable `$#` tells you how many arguments were on the command line; if there were three arguments, for example, they would be available as `$1`, `$2`, and `$3`.
In the command line `myscript.sh a b c`, then, `$#=3`, `$0=myscript.sh`, `$1=a`, `$2=b`, and `$3=c`.

See example [args.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/main/args.sh).
It prints `$#` and `$0` as well as `$1`, `$2`, `$3`.
It double-quotes the variable substitution, but also prints single-quotes so we can easily see the beginning and ending of each string substituted.
Try it with no arguments, and one or two arguments.

The two variables `$*` and `$@` both provide the list of command-line arguments, but with subtle differences; again, look at example [args.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/main/args.sh), to see the difference.

Let's try it on a command with four simple arguments.

```bash
$ ./args.sh John Paul George Ringo
There are 4 arguments to ./args.sh.
The first  argument is 'John'
The second argument is 'Paul'
The third  argument is 'George'

for arg in $* (likely not what you expect)
John
Paul
George
Ringo

for arg in "$*" (likely not what you expect)
John Paul George Ringo

for arg in $@ (likely not what you expect)
John
Paul
George
Ringo

for arg in "$@" (likely best for all common scripts)
John
Paul
George
Ringo
$
```

You can't tell the difference between three forms of the for loop, but now let's try it on a command with arguments that have embedded spaces.

```bash
$ ./args.sh "John Lennon" "Paul McCartney" "George Harrison" "Ringo Starr"
There are 4 arguments to ./args.sh.
The first  argument is 'John Lennon'
The second argument is 'Paul McCartney'
The third  argument is 'George Harrison'

for arg in $* (likely not what you expect)
John
Lennon
Paul
McCartney
George
Harrison
Ringo
Starr

for arg in "$*" (likely not what you expect)
John Lennon Paul McCartney George Harrison Ringo Starr

for arg in $@ (likely not what you expect)
John
Lennon
Paul
McCartney
George
Harrison
Ringo
Starr

for arg in "$@" (likely best for all common scripts)
John Lennon
Paul McCartney
George Harrison
Ringo Starr
$
```

Study the difference of each case.
You should use `"$@"` to process command-line arguments, nearly always, because it retains the structure of those arguments.

As a shorthand, `for arg` is equivalent to `for arg in "$@"`.

> My choice of the variable name `arg` is immaterial to the shell.

