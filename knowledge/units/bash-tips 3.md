More tips about bash programming.

Note there are some good Bash references on the [systems page](https://github.com/CS50Dartmouth21FS1/home/blob/main/logistics/systems.md#bash);
although the [Bash reference manual](https://www.gnu.org/software/bash/manual/bash.html) is excellent, keep in mind it is for the latest version of Bash and `plank` may be running an older version that differs slightly.

## The 'shift' command

Sometimes you want to discard the first argument (or two) and then loop over the remainder.
The `shift` command is there to help!
Play with example [shifter.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/main/shifter.sh):

```bash
echo I see $# arguments: "$@"
shift
echo now I see $# arguments: "$@"
```

## Printing error messages

You might need to inform the user of an error; in this example, the 2nd argument is supposed to be a directory and the script found that it is not:

```bash
echo 1>&2  Error: "$2" should be a directory
```

Here we see how to push the output of `echo`, normally to stdout (`1`), to the stderr (`2`) instead, by redirecting the stdout to the stderr using the confusing but useful redirect `1>&2`, which means "make this command's stdout go to the same place as the stderr."


## Checking arguments

When writing scripts it is important to write defensive code that checks whether the input arguments are correct.
Below, the program verifies that the command has exactly three arguments, using the 'not equal to' operator.

```bash
if [[ $# -ne 3 ]]; then
   echo 1>&2 Usage: incorrect argument input
   exit 1
fi
```

Notice also that the script then exits with a non-zero status.



## Simple debugging tips

**Print debugging information:**
When you run a script you can use `printf` or `echo` to print debugging information to the screen.
I found it helpful to define a function `debugPrint` so I can turn on and off all my debug statements in one place:

```bash
# print the arguments for debugging; comment-out 'echo' line to turn it off.
function debugPrint() {
#    echo "$@"
    return
}
...
debugPrint starting to process arguments...
for arg; do
	debugPrint processing "$arg"
	...
```

**Look upward for syntax errors:**
If you get a syntax error; for example:

```
./ziplab1.sh: line 18: syntax error near unexpected token `else'
./ziplab1.sh: line 18: `else'
```

The error is on or before line 18.

> In `emacs` edit the file `./ziplab1.sh` and go to line 18 using the sequence of key strokes `ESC g` -- that is, hit the `ESC` key and hit `g`.
> (If you did not install the customized `~cs50-dev/dotfiles/emacs` file as your own `~/.emacs`, you may need to hit `g` twice.)
> Then, enter the line number 18 and you will be brought to that line.

In this example, the actual error was on line 13, not 18; on line 13 the `if` statement began, but I forgot the semicolon before `then`...
the shell finally realized a problem when it reached the `else` command at line 18.
So you may need to work backwards through the code, looking carefully to find the syntax problem.

**Backup copies:**
Every time you launch `emacs` to edit a file, it saves a backup copy of that file.
For example, when you edit `foo.sh` and save it, `emacs` saves the pre-editing version in `foo.sh~`.
If you're later wondering what changed,

```bash
$ diff foo.sh~ foo.sh
```

will print the differences between the two files.

## 'let' me do arithmetic!

The `let` command carries out arithmetic operations on variables.

```bash
$ let a=1

$ let b=2

$ let c = a + b
-bash: let: =: syntax error: operand expected (error token is "=")

# ... note, the let command is sensitive to spaces.

$ let c=a+b

$ echo $c
3

$ echo "a+b=$c"
a+b=3

$ echo "$a+$b=$c"
1+2=3

$ let a*=10  # equivalent to  let a=a*10
$ echo $a
10

```


## Temporary files

If your script needs to create some temporary files to do its work, it is good practice to create those files in a place *other than the current directory*, and with a filename that is unlikely to be used by another script - even another concurrently running copy of your script.

The directory `/tmp` is the conventional place to put temporary files - but it is readable and writable by everyone - so it's not a great place to put important files!
We recommend you create and use your own tmp directory:

```bash
$ mkdir ~/tmp
```

Savvy scripts also recognize you might run multiple copies of the script, simultaneously; such scripts include `$$`, an automatic shell variable containing a number unique to that process, as part of the filename.
For example, a script `print` might do the following:

```bash
#!/bin/bash
# build up an output file, then print it.
# name of temporary file includes our process id $$
tmpfile=~/tmp/print$$
echo > $tmpfile

for arg
do
	# print a nice header then the file
	echo "======================" >> $tmpfile
	echo "$arg" >> $tmpfile
	cat "$arg" >> $tmpfile
	echo >> $tmpfile
done

lpr $tmpfile		# print the result
rm -f $tmpfile	# clean up after ourself
exit 0
```

This script defines a variable `tmpfile` once - and uses it throughout, for clarity and consistency throughout the script.

Just remember to clean out your tmp directory from time to time!


## Catching interrupts, cleaning up

Many scripts create intermediate or temporary files, and might leave a mess if interrupted part-way through their operation.

The `trap` command can catch such interrupts, such as those caused by the user typing `ctrl-C` at the keyboard while the script works.
It is good form to catch this interrupt and clean up before exiting.
In the above example, we would extend the above example as follows:

```bash
# name of temporary file includes our process id $$
tmpfile=~/tmp/print$$
trap "rm -f $tmpfile" EXIT
```

This `trap` command gives the shell a command to run whenever the script exits, for any reason (whether due to an `exit` command or due to an interrupt that kills the process).
Very handy!
Notice that I define the `trap` *immediately* after defining the variable name, so that it will be in effect whenever the temporary file is later created.
The `-f` flag ('force') to `rm` causes it to override some kinds of errors, notably, to not complain if the `$tmpfile` does not yet exist.

Sometimes you need a whole directory for your temporary use:

```bash
tmpdir=~/tmp/printdir$$
trap "rm -rf $tmpdir" EXIT
mkdir -p $tmpdir
cd $tmpdir
```

Here I used `mkdir -p` to make the directory, and `rm -rf` to recursively remove it.
