

## <a id="gets-fgets">gets and fgets</a>

There is a saying - you learn from your mistakes, so make lots of them.
There is another one: don't make the same mistake twice.
The use of the stdio function `gets()` is a mistake.
Lots of programmers have made this mistake, and caused headaches for millions of computer users around the world.
**The lesson: never use `gets()`!**

The reason: `gets()` reads a line from `stdin` into a character buffer - an array of characters provided by the caller - but has no idea how big is the buffer.
If the input contains more characters than fit in the buffer, `gets()` will happily continue to write characters into memory beyond the end of the buffer.
But there is likely something else important stored in that region of memory!
The resulting *buffer overflow* has been the mechanism for many cyberattacks, in which hackers craft clever strings that will overflow an input buffer and write just the right sort of data or code into adjacent memory, causing the program to do something its programmer never intended - but which serves the hacker's interest.

The stdio library includes a safer function, `fgets()`, which reads from any FILE into a character buffer... but requires the user to provide the length of the buffer.  As long as the programmer supplies the right size with the right buffer, `fgets` will never overflow the buffer.

I won't go into more detail here, because it's too much of a digression... in this class, we won't use `gets` (because it's so dangerous nobody should ever use it) or `fgets` (because we developed more convenient alternatives, `readline()` and `readlinep()`).

## Buffer overflows

Let's look at an example that could have been named `really-bad-code.c`.

**Example: [buffer-overflow.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/buffer-overflow.c)**

Warning! this does not follow CS50 style!
Even the compiler warns us about the danger:

```bash
$ mygcc buffer-overflow.c -o overflow
/tmp/cchc9T0P.o: In function `main':
buffer-overflow.c:54: warning: the `gets' function is dangerous and should not be used.
$ 
```

Let's look at the output when running the program first with `gets()` and then with the safer `fgets()`.
If we run the code with `gets()` we get a segmentation fault when entering 60 characters.

```
$ ./overflow 
Hello is less than - Hello there

The length of string1 is 5 characters
The length of string2 is 11 characters

After concatenation, string1 contains the string value
Hello there World!
The length of this string is 18 characters

Please enter a line of text for string2, max 50 characters: 012345678901234567890123456789012345678901234567890123456789
Thanks for entering 012345678901234567890123456789012345678901234567890123456789
After copying string2 to string1 the string value in string1 is:
012345678901234567890123456789012345678901234567890123456789
The length of string1 is 60 characters


The starting address of the string1 string is: 0x7ffeb9b92d90

The starting address of the string2 string is: 0x7ffeb9b92dd0
*** stack smashing detected ***: <unknown> terminated
Aborted (core dumped)
$ file core
core: ELF 64-bit LSB core file x86-64, version 1 (SYSV), SVR4-style, from './overflow', real uid: 23925, effective uid: 23925, real gid: 168108, effective gid: 168108, execfn: './overflow', platform: 'x86_64'
$
```

This is a bad program!
The basic idea of the program is to accept and manipulate strings using arrays of chars.
However, there is a serious flaw in the program.
Some older books use the function `gets()`; it is a seriously dangerous function call.
***Do not use gets()!***

The program defines a buffer of 50 chars in length.
The user types in characters from the keyboard and they are written to the buffer, i.e., `string1` and `string2`.

The input parameter to `gets()` is the name of the array (which is a pointer - more on pointers later).
The function does not know how long the array is!
It is impossible to determine the length of `string1` and `string2` from a pointer alone.

If we run the program and type in 50 characters, including the newline, all is safe.
But if we type 51 or 60 or more characters, we overrun or 'overflow' the buffer.
We end up writing past the end of the array!
Fortunately, modern C compilers insert some code to detect the worst cases of such overflows, which tends to "smash the stack"; above you see the program exited when it detected that result, and "dumped core" (saved an copy of the memory from the running program, for later analysis and debugging).
Unfortunately, such detection methods are not perfect ... and even when they work, they crash your program.

This overflow can happen even without calling an unsafe function such as `gets()`, so it's an important lesson to learn.
Buffer overflows can have rather spectacular results!

Bugs often happen at boundary conditions and one important boundary is the end of the array.
If we overwrite `string1`, we might write into `string2`.
Recall that, by convention, C strings are terminated by `\0` (aka null character).
If this character is overwritten then a piece of code operating on the array will keep on scanning until it finds a `\0`.

If we run this code and type in more than 50 chars (as we did above) anything can happen; for example: 1) the code could work with no visible affect of the bug; 2) immediate segfault; 3) segfault later in the code stream; 3) mistakes happen in unrelated functions (e.g., strcat() in our code).

Some books use `gets()` and promote its use.
Just Say NO!
Instead, use the safe `fgets()` as it is a buffer-safe function.
Its prototype is:

```c
    char *fgets(char *s, int size, FILE *stream);
```

It requires you to identify *which file*, yes, but more importantly, it requires you to identify the size of the character buffer into which it will write characters; `fgets` will not write more characters than the size of the buffer.

Example:

```c
    fgets(buf, sizeof(buf), stdin);
```

The `fgets()` function shall read bytes from stream into the array pointed to by `buf`, until `sizeof(buf)-1` bytes are read, or a newline is read and transferred to `buf`, or an end-of-file condition is encountered.
The string is then terminated with a null byte.

We replace `gets()` with `fgets()` in the above code and now we are safe.

