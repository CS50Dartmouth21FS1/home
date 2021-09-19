The 'for-loop' construct is good for looping through a series of strings but not that useful when you do not know how many times the loop needs to run.
The `while do` command is perfect for this.

> I replaced my shell prompt with `$ ` for readability.

### guess1: while loop, prompted user input

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4813c35b-d598-4ae0-94d2-acfc016f900c)**

The contents of [guess1a.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/guess1a.sh) use the 'while-do' construct.
The script allows the user to guess a number between 1-100.

```bash
#!/bin/bash
#
# guess1.sh
# 
# Description: The user tries to guess a number between 1-100 
# This is not a good program. There is no check on what the
# user enters; it might be outside the range.
# Heck - it might not even be a number and might be empty!
# Some defensive programming would check the input.
# 
# Input: The user enters a guess
#
# Output: Status on the guess

echo "I'm thinking of a number between 1-100."
# The correct answer.
answer=31

read -p "Guess my number: " guess

while [ $guess != $answer ]; do
    echo "Wrong! try again"
    read -p "Guess my number: " guess
done

echo Correct!
exit 0
```

This script has user-defined variables `number` and `guess`.
It introduces the `read` command, which prompts (`-p`) and waits for user input, placing that user input into the named variable (here `guess`).
Finally, note the semicolon after the `while` command and before the `do` command.
As with the `if` command and its `then` branch, or the `for` command and its `do`, we could have put `do` on the next line if we prefer that style.

```bash
$  ~/cs50-dev/shared/examples/guess1a.sh  
I'm thinking of a number between 1-100.
Guess my number: 40
Wrong! try again
Guess my number: 1
Wrong! try again
Guess my number: 0
Wrong! try again
Guess my number: 100
Wrong! try again
Guess my number: 31
Correct!
$ 
```

Notice the command used by `while` is `[`, that is, the `test` command.
As written, the expression `[ $guess != $answer ]` compares the contents of the two variables *as strings*.
In this game, though, we want the user to guess a number, so we really should compare them *as numbers*.
We need to use the more advanced bash operator `[[`, which leads us to [guess1b.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/guess1b.sh).
The loop now looks like:

```bash
while [[ $guess -ne $answer ]]; do
    echo "Wrong! try again"
    read -p "Guess my number: " guess
done
```

We use the `-ne` (not equal) operator because that performs a numeric comparison, whereas the `!=` operator performs a string comparison.
For more info, see the relevant section of the [Bash reference manual](https://www.gnu.org/software/bash/manual/bash.html#Bash-Conditional-Expressions).


### guess2: better game

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=929ba055-7374-4e0e-ba5e-acfc01717e04)**

Let's make this game more interesting by giving the user guidance; notice the `if` statement inside the loop.
See [guess2.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/guess2.sh):

```bash
while [[ $guess -ne $answer ]]; do
    # The -gt treats the comparison as numeric rather than alpha.
    if [[ $guess -gt $answer ]]; then
        echo too high!
    else
        echo too low!
    fi

    read -p "Guess my number: " guess
done
```

Here we use the `-gt` (greater than) operator to obtain a numeric comparison.
The `>` operator would do an alphabetic comparison.
For example: `6 -gt 10` is false but `6 > 10` is true.

By the way, you can use `diff` to see differences between versions of these examples:

```diff
$ diff guess1b.sh guess2.sh
3c3
< # guess1.sh
---
> # guess2.sh
6,11c6,7
< # We change the expression to use [[ and -gt for arithmetical comparison.
< #
< # This is not a good program. There is no check on what the
< # user enters; it might be outside the range.
< # Heck - it might not even be a number and might be empty!
< # Some defensive programming would check the input.
---
> # This time, we guide the user toward an answer.
> # It still needs some defensive checks on the input!
24c20,26
<     echo "Wrong! try again"
---
>     # The -gt treats the comparison as numeric rather than alpha.
>     if [[ $guess -gt $answer ]]; then
>         echo too high!
>     else
>         echo too low!
>     fi
> 
$
```

### guess3: functions

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=45719e60-d670-4bd5-a575-acfc01724d40)**

Like most procedural languages, shell scripts have structure and function support.
Typically, it is a good idea to use functions to make scripts more readable and structured.
In what follows, we add a function to create [guess3.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/guess3.sh).
To demonstrate function arguments, I updated the prompt to remind the user of the expected range of their guess.

```bash
...

# Ask the user to guess, and fill global variable $guess with result.
# usage: askguess low high
#   where [low-high] is the range of numbers in which they should guess.
function askguess() {
    read -p "Guess my number (between $1 and $2): " guess
}

echo "I'm thinking of a number between 1-100."
# The correct answer.
answer=31

# ask them once
askguess 1 100

while [[ $guess -ne $answer ]]; do
    # The -gt treats the comparison as numeric rather than alpha.
    if [[ $guess -gt $answer ]]; then
        echo too high!
    else
        echo too low!
    fi

    # ask them again
    askguess 1 100
done
...
```

Notice that defining a function effectively adds a new command to the shell, in this case, `askguess`.
And that command can have arguments!
And those arguments are available within the function as if they were command-line arguments `$1`, `$2`, and so forth.
All other variables are treated as 'global' variables, like `guess` in this example.

Try this script; it's very fragile.
See what happens when you enter multiple numbers - or any multiple words - like `4 5 6` or `ab cd`.
Why does that happen?


### guess4: defensive input processing

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f9edf185-81f6-478f-b093-acfc01735bf3)**

Finally, we should be careful about what the user enters as a guess.
It might be an empty string, or a non-number, or multiple words.
We should never use `$guess` without quoting it, `"$guess"`, because otherwise the shell will paste all of its words separately... likely leading to syntax errors.

In [guess4.sh](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/guess4.sh) we now see why it was important to move the user input into a function: because that function can also encapsulate the error-checking code.

```bash
function askguess() {
    read -p "Guess my number (between $1 and $2): " guess
    # Validate their input; it must not be empty, or contain a non-digit.
    # Note we quote $guess because it might be empty or multi-word.
    if [[ "$guess" == "" || "$guess" =~ [^0-9] ]]; then
        echo bad number...
        guess=0
    fi
    # at this point, $guess is guaranteed to be a number.
}
```

Notice the use of the `=~` comparison operator, which means "matches pattern".
The pattern (a *range expression*) `[0-9]` would match any digit, and a range expression can be negated by prefixing with `^`; thus `[^0-9]` matches any character that is not a digit.
Thus, if variable `guess` contains any character that is not a digit, it will match that pattern... and lead to printing "bad number...".

> Note: these patterns are *regular expressions*, not glob patterns.
> So, for example, if you want to test whether `$f` is a filename ending in `.sh`, you need `[[ "$f" =~ .*\.sh$ ]]` not `[[ "$f" =~ *.sh ]]`.
> That regular expression means "match zero or more instances (`*`) of any character (`.`), followed literally by a dot (`\.`), followed by the characters `sh` at the end of the string.)

## Comments

Shell scripts, like any program, need to be well commented!
Our examples demonstrate several of the best practices for commenting code:

- Comments should clarify the code, not obscure it.
- They should enlighten, not impress.
- If you used a special algorithm or text, mention it and give a reference!
- Don't just add noise or chitchat.
- Say in comments what the code cannot.
