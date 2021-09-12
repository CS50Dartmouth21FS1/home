In this unit we discuss the *special characters* interpreted by the shell, how to *quote* them to prevent the shell from interpreting them.
We also explore the power of the `tr` and `sed` commands.

> I replaced my shell prompt with `$` for readability.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d856c201-41e5-4d62-a945-acfb016e8d0b)**

There are a number of special characters interpreted by the shell - spaces, tabs, wildcard ('globbing') characters for filename expansion, redirection symbols, and so forth.
Special characters have special meaning and cannot be used as regular characters because the shell interprets them in a special manner.
These special characters include:

``& ; | * ?  ` " ' [ ] ( ) $ < > { } # / \ ! ~ ``

We have already used several of these special characters.
Don't try to memorize them at this stage.
Through use, they will become second nature.
We will just give some examples of the ones we have not discussed so far.


## Quoting

If you need to use one of these special characters as a regular character, you can tell the shell not to interpret it by escaping or quoting it.
To *escape* a single special character, precede it with a backslash `\`; in the example above, we used escaped the character `*` with `\*` to prevent the shell from interpreting `*.html` as a glob pattern; instead, we wanted that pattern to pass through unchanged to `find`, which does its own pattern matching.
To escape multiple special characters (as in `**`), quote each: `\*\*`.
You can also *quote* using single quotation marks such as `'**'` or double quotation marks such as `"**"` - but these have subtlety different behavior.
You might use this form when quoting a filename with embedded spaces: `"My Homework.txt"`.

You will often need to pass special characters as part of arguments to commands and other programs - for example, an argument that represents a pattern to be interpreted by the command; as happens often with `find` and `grep`.

There is a situation where single quotes work differently than double quotes.
If you use a pair of single quotes around a shell variable substitution (like `$USER`), the variable's value will not be substituted, whereas it would be substituted within double quotes:

```bash
$ echo "$LOGNAME uses the $SHELL shell and his home directory is $HOME."
d31379t uses the /bin/bash shell and his home directory is /thayerfs/home/d31379t.
$ echo '$LOGNAME uses the $SHELL shell and his home directory is $HOME.'
$LOGNAME uses the $SHELL shell and his home directory is $HOME.
$ 
```

***Example 1.***
Double-quotes are especially important in shell scripts, because the variables involved might have been user input (a command-line argument or a keyboard input) or might have be a file name or output of a command; such variables should *always* be quoted when substituted, because spaces (and other special characters) embedded in the value of the variable can cause confusion.
Thus:

```bash
directoryName="Homework three"
...
mkdir "$directoryName"
mkdir $directoryName
```

Try it!

***Example 2.***
Escapes and quoting can pass special characters and patterns passed to commands.

Suppose I have a list of email addresses, one per line, in a file.
Some email programs, or websites, require these to be comma-separated.
If I have the above text in my clipboard, I can change those newline characters to commas:

```bash
$ cat learning-fellows 
Jackson.R.McGary.23@dartmouth.edu
Jacob.M.Chen.22@dartmouth.edu
Jacob.E.Werzinsky.22@dartmouth.edu
Kelly.B.Westkaemper.22@dartmouth.edu
Rylee.R.Stone.21@dartmouth.edu
Darren.Gu.22@dartmouth.edu
Rachael.E.Williams.23@dartmouth.edu
Wending.Wu.23@dartmouth.edu
William.P.Dinauer.23@dartmouth.edu
$ tr \\n , < learning-fellows 
Jackson.R.McGary.23@dartmouth.edu,Jacob.M.Chen.22@dartmouth.edu,Jacob.E.Werzinsky.22@dartmouth.edu,Kelly.B.Westkaemper.22@dartmouth.edu,Rylee.R.Stone.21@dartmouth.edu,Darren.Gu.22@dartmouth.edu,Rachael.E.Williams.23@dartmouth.edu,Wending.Wu.23@dartmouth.edu,William.P.Dinauer.23@dartmouth.edu,$ 
```

> On Unix a single special character called 'newline' is what *defines* the end of one line and the beginning of the next.
A common syntax for the newline character, in programming languages, is `\n`.
But the `\` is special, in bash, so we need to escape it, um, with `\`; thus we have `\\n`.

The `tr` command filters stdin to stdout, translates each instance of the character given in the first argument (`\\n`) to the character given in the second argument (`,`).
Here, `tr` spit out the translated input - on one very long line that does not end with a newline.
Indeed, because it did not end in a newline, the shell prompt appears at the end of that line!
Let's put the output in a file, and add a newline:

```bash
$ tr \\n , < learning-fellows  > comma-sep-emails
$ echo >> comma-sep-emails 
$ cat comma-sep-emails 
Jackson.R.McGary.23@dartmouth.edu,Jacob.M.Chen.22@dartmouth.edu,Jacob.E.Werzinsky.22@dartmouth.edu,Kelly.B.Westkaemper.22@dartmouth.edu,Rylee.R.Stone.21@dartmouth.edu,Darren.Gu.22@dartmouth.edu,Rachael.E.Williams.23@dartmouth.edu,Wending.Wu.23@dartmouth.edu,William.P.Dinauer.23@dartmouth.edu,
$ 
```

I can then copy-paste that result into the pesky email program.

Outlook and Microsoft tools want *semicolons*, not commas; sigh.
But the semicolon is also special to bash; so we must escape it too:

```bash
	tr \\n \;
```

## sed

An even more powerful filtering tool - the stream editor called `sed` - allows you to transform occurrences of one or more patterns in the input file(s), producing the edited version on stdout:

```bash
	sed pattern [file]...  
```

For example, suppose I have a fancier list of addresses:

```bash
$ cat learning-fellows
Darren Gu         <Darren.Gu.22@dartmouth.edu>
David Kotz        <David.F.Kotz@dartmouth.edu>
Jack McGary       <Jackson.R.McGary.23@dartmouth.edu>
Jacob Chen        <Jacob.M.Chen.22@dartmouth.edu>
Jacob Werzinsky   <Jacob.E.Werzinsky.22@dartmouth.edu>
Kelly Westkaemper <Kelly.B.Westkaemper.22@dartmouth.edu>
Rachael Williams  <Rachael.E.Williams.23@dartmouth.edu>
Rylee Stone       <Rylee.R.Stone.21@dartmouth.edu>
Wendell Wu        <Wending.Wu.23@dartmouth.edu>
William Dinauer   <William.P.Dinauer.23@dartmouth.edu>
$ 
```

Remove myself, and excess white space:

```bash
$ sed -e /David.F.Kotz/d -e 's/\t/ /g' -e 's/  */ /g' learning-fellows
Darren Gu <Darren.Gu.22@dartmouth.edu>
Jack McGary <Jackson.R.McGary.23@dartmouth.edu>
Jacob Chen <Jacob.M.Chen.22@dartmouth.edu>
Jacob Werzinsky <Jacob.E.Werzinsky.22@dartmouth.edu>
Kelly Westkaemper <Kelly.B.Westkaemper.22@dartmouth.edu>
Rachael Williams <Rachael.E.Williams.23@dartmouth.edu>
Rylee Stone <Rylee.R.Stone.21@dartmouth.edu>
Wendell Wu <Wending.Wu.23@dartmouth.edu>
William Dinauer <William.P.Dinauer.23@dartmouth.edu>
$ 
```
Remove myself, remove names, print a comma-sep list of addresses:

```bash
$ sed -e /David.F.Kotz/d -e 's/.*<//' -e 's/>.*/,/' learning-fellows
Darren.Gu.22@dartmouth.edu,
Jackson.R.McGary.23@dartmouth.edu,
Jacob.M.Chen.22@dartmouth.edu,
Jacob.E.Werzinsky.22@dartmouth.edu,
Kelly.B.Westkaemper.22@dartmouth.edu,
Rachael.E.Williams.23@dartmouth.edu,
Rylee.R.Stone.21@dartmouth.edu,
Wending.Wu.23@dartmouth.edu,
William.P.Dinauer.23@dartmouth.edu,
$ 
```

The above uses the `-e` switch to `sed`, three times, each of which specifies one sed command.

Each sed command applies to lines matching a pattern, or to a *range* of lines matching a pattern.
The simplest pattern is a line number (like `1` to refer to the first line), and the simplest range is a pair of line numbers (like `1,10` to refer to the first ten lines).
But either or both can be a pattern (like `/^begin:/` to refer to a line starting with the text `begin:`), or the special number `$` representing the last line of the file.
Patterns are actually [regular expressions](http://sed.sourceforge.net/sedfaq3.html#s3.1.1); I use some above.

After the line number, or range, comes a single-letter command.
The most common sed commands are

 * `d` deletes lines matching the pattern
 * `s` substitutes text for matches to the pattern.
 * `p` prints lines matching the pattern (useful with `-n`)

See `man sed` for more detail.

***Example 4.***

I saved the email addresses of students enrolled in CS50 in the files `section1` and `section2`.
Each line is of the form `First.M.Last.XX@Dartmouth.edu`.

```bash
$ ls section?
section1  section2
$ wc -l section?
  54 section1
  55 section2
 109 total
$ 
```

Recall that the shell expands `section?` to `section1 section2`, using filename globbing, so `sed` actually receives two filename arguments; it processes each in turn.

Let's suppose you all decide to move to Harvard (gasp!).

```bash
$ sed s/Dartmouth/Harvard/ section?
...
$ sed -e s/Dartmouth/Harvard/ -e 's/\.[0-9][0-9]//' section?
...
```

The second form removes the dot and two-digit class number.
Notice how I quoted those patterns from the shell, and even escaped the dot from sed's normal meaning (dot matches any character) so sed would look for a literal dot in that position.
The dollar `$` constrains the semicolon match to happen at the end of the line.

Here's another fun pipe: count the number of students from each class (leveraging the class numbers in email addresses):

```bash
$ cat section? | tr -c -d 0-9\\n | sed 's/^$/other/'  | sort  | uniq -c | sort -nr
     50 23
     38 24
     15 22
      5 21
      1 other
$ 
```

See `man sed`,
 the [sed FAQ](http://sed.sourceforge.net/sedfaq.html),
 or the [sed home page](https://www.gnu.org/software/sed/)
 for more info.
You'll want to learn a bit about *regular expressions*, which are used to describe patterns in sed's commands; see [sed regexp info](http://sed.sourceforge.net/sedfaq3.html#s3.1.1).
