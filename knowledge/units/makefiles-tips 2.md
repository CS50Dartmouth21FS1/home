A collection of small tips for using Makefiles.

A Makefile is a sequence of *macro definitions* and *recipes*.

## Macros

*Macros* are like variables except they can't change value; they are a name that expands to a string.
Every CS50 Makefile includes this macro:

```make
CFLAGS = -Wall -pedantic -std=c11 -ggdb 
```

From the point where a macro is defined, downward in the file, that macro can be substituted with its value as follows: `$(CFLAGS)`.
If the macro name is only a single letter, the parentheses are not needed; thus a macro `L` can be substituted by `$L`.
We use this nice shortcut in CS50 makefiles:

```make
C = ../common
L = ../libcs50
LLIBS = $C/common.a $L/libcs50.a
```

If a macro `xyz` is not defined, `$(xyz)` is the empty string, not an error; it is thus hard to catch typos!
Be careful to proofread your Makefile.

Macro names are conventionally written in UPPER case, but need not be.

## Recipes

A *recipe* is a sequence of *commands* to build a *target* when one or more of its *dependencies* is newer than the target.
The form is

```make
target: [dependency]...
	[command]
	[...]
```

Thus, there are zero or more dependencies, and zero or more commands.
Make only runs the commands when the one or more of its *dependencies* is newer than the target, that is, the file modification time is more recent than the target's modification time.

In CS50, this is a common recipe:

```make
foo: foo.o $(LLIBS)
        $(CC) $(CFLAGS) $^ $(LIBS) -o $@
```

This rule tells Make how to build executable program `foo` from its components, which are `foo.o` and a list of local libraries defined by the macro `LLIBS`, and a list of global libraries defined by the macro `LIBS`.
The special macro `$^` expands to the set of dependencies.
The special macro `$@` expands to the name of the target.
Make implicitly knows how to build `foo.o` from `foo.c`.

If there are zero commands, the recipe simply adds to the set of dependencies for that target.

If there are zero dependencies, then Make always runs the commands when asked to build that target.

When running `make` without arguments, Make triggers the first recipe.
It is thus common to put an explicit first recipe, with no commands, e.g.,

```make
all: indexer indextest
```

Thus `make` or `make all` will be the same as `make indexer indextest`, but much easier to type.


## Spaces vs tabs

In a Makefile, the *commands* must be indented by a single *tab*, not by a sequence of spaces.
If you use spaces instead of (or in addition to) tabs, Make will get confused or complain.

## Empty commands

I once saw a Makefile in which a recipe had no apparent commands listed underneath... but it actually had a line comprised only of a tab character.
Visually it looked like an empty line, but Make treated it as an empty command; so when Make triggered that recipe it ran the empty command... it did nothing, without complaint.
Very confusing!

Emacs recognizes Makefiles, colors the syntax appropriately, and highlights such a situation so you'll notice and fix it.

## Ignoring errors

If a recipe is running a command that might exit non-zero, and you don’t want Make to complain in that situation, prefix the command with `-`, like the following:

```make
test:
	-bash testing.sh &> testing.out
```

## Not printing commands

If a recipe is using `echo` as one of its commands, e.g., to document what it is doing, prefix the command with `@`, like the following:

```make
test:
	@echo run the basic test suite...
	-bash testing.sh &> testing.out
	@echo run the valgrind suite...
	-bash valgrind.sh &> valgrind.out
```

The `@` prevents Make from printing the command line, which is good because `echo` is about to print the same thing!
