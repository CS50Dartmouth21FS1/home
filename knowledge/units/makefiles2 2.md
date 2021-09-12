A collection of useful tips about `make` and Makefiles.

## Make command line

Make supports many command-line arguments.
See `man make`.
Here are the most important:

`-f file` use `file` instead of default `Makefile`

`-n` Determine which commands need to be run, but don't actually execute any of them, just print them.

`-k` while a particular target and the things that depend on it might have failed, continue trying to make the other dependencies of these targets anyway.
(My emacs command uses this flag; see below.)

`-C dir` or `--directory=dir` tells Make to change its working directory (as if it internally runs `cd dir`) and then look for a Makefile (i.e., it will read from `dir/Makefile`).
This option is useful for running Make in a subdirectory.

## Automatic variables

The make utility also provides some useful [automatic variables](http://www.gnu.org/software/make/manual/make.html#Automatic-Variables):

`$@`   name of the current target

`$?`   the list of dependencies that are newer than the target

`$^`	the list of dependencies for this target

For example, we could rewrite our `sorter` target as follows

```makefile
sorter: sorter.o bag.o readlinep.o
	$(CC) $(CFLAGS) $^ -o $@
```

Although useful, I find the style we used in our complete example to be more readable.

## for emacs users

If you use emacs on plank, we've configured it with a keystroke `^X-c` (ctrl-X c), which tells emacs to run `make -k` in a separate subwindow.
If you get any compiler errors, type the keystroke ``^X-` `` (ctrl-X backquote) and emacs jumps to the right file and right line.
It's handy!

## Non-compilation makefiles

You can use makefiles to solve many of your daily challenges involving a sequences of dependent actions:

-   running test shell scripts during development and regression testing
-   creating documents with LaTeX
-   maintaining webpages (staging and live directories)
-   automated documentation generation
-   source code management

I use a Makefile to compile and push the CS50 website to the webserver!


## Documentation

You can view the [GNU documentation](http://www.gnu.org/software/make/manual/make.html), the manual pages for `make`, Steve Talbot's "oldie but goodie" book "*Managing projects with make*" [updated](http://shop.oreilly.com/product/9780596006105.do) in 2009 by Robert Mecklenburg for GNU `make`, or any of the variety of online tutorials for `make`.

