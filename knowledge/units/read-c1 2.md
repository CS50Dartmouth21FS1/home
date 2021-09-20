## C style guidelines

You really *must* read the [CS50 coding style guidelines](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/logistics/style.md).

## Reference books

We elected not to require a specific textbook on C.
There are many to choose from, including a good text online (see the [systems page](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/logistics/systems.md)).
The Harbison and Steele book is highly recommended as a reference resource.
But it is not a tutorial.
When I do reference it, I will use the shorthand "H&S".
Past incarnations of this course used the text by Bronson, *[A First Book of ANSI C](http://www.amazon.com/First-Book-Fourth-Introduction-Programming/dp/1418835560/ref=sr_1_1?ie=UTF8&qid=1326594741&sr=8-1)*.
It's not bad, and there are lots of others, including some that you can get electronically, like Prinz's *[C in a Nutshell](http://www.amazon.com/C-Nutshell-OReilly-ebook/dp/B0043GXMRK/ref=sr_1_2?ie=UTF8&qid=1326594935&sr=8-2)* (O'Reilly).

## Manuals for stdio, stdlib

C programming depends on a suite of standard libraries for input/output, strings, math, memory allocation, and so forth.
Most or all of these functions are documented in man pages, just like shell commands.
Try `man strcpy`, for example.

For some C functions there are shell commands with identical names; if you type `man printf`, for example, you'll see the man page for the bash `printf` command and not the C function `printf()`.
You can ask `man` to look only for library functions (section 3 of the manual) with `man 3 printf`.
