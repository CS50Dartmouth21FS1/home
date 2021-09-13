**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ddb533aa-1a98-45f1-84d3-ad0a0011f32d)**

Earlier we developed a simple `readline()` function (and its companion `freadline()` to read from a file), which in [readline.h](https://github.com/CS50Dartmouth21FS1/examples/blob/main/readline.h) has prototype

```c
extern bool readLine(char *buf, const int len);
```

This would read from stdin into `buf` until it encountered a newline, or the buffer (of length `len`) was full.
In the latter case it would return false and discard the whole line of input.

To be more accommodating, we developed `readLinep` (and its companion `freadLinep`).
In [readlinep.h](https://github.com/CS50Dartmouth21FS1/examples/blob/main/readlinep.h) we see the prototypes:

```c
extern char* readLinep(void);
extern char* freadLinep(FILE *fp);
```

Notice that `readLinep` is an "inline" function, which means that it is compiled directly into the code wherever it is called, rather than being a true function.
(That's why it has to be declared `static`: because every file that includes `readLinep.h` needs its only private 'copy' of `readLinep`.)  It's a simple wrapper that calls `freadLine(stdin)`.

## freadLinep

The definition (implementation) of `freadLinep` is in [readlinep.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/readlinep.c).

Notice it returns a string - and yet it takes no pointer to a buffer.
Instead, it allocates heap memory and returns a pointer to the new string; it returns NULL on error.
The caller is later responsible to `free` that pointer.

The core of the `freadLinep()` code is below.
Notice:

* we start off by allocating a character array from the heap, using `malloc`, hoping it is big enough to hold common input lines.
* we then loop, getting characters from the input file, until we reach EOF or a newline character.
* we insert each new character into `buf[pos]`, bumping the index `pos` each time through the loop.
* but we carefully monitor `pos` and its relation to the current length of the buffer; if the new character would cause us to overflow the buffer, we ask `realloc()` to grow the buffer by one more byte.
* we return NULL if there is any memory allocation error, or if we reach EOF.
* otherwise we return a pointer to the buffer.

```c
  // allocate buffer big enough for "typical" words/lines
  int len = 81;
  char* buf = malloc(len * sizeof(char));
  if (buf == NULL) {
    return NULL;        // out of memory
  }

  // Read characters from file until newline or EOF, 
  // expanding the buffer when needed to hold more.
  int pos;
  char c;
  for (pos = 0; (c = fgetc(fp)) != EOF && (c != '\n'); pos++) {
    // We need to reserve buf[pos+1] for the terminating null
    // and buf[len-1] is the last usable slot, 
    // so if pos+1 is past that slot, we need to grow the buffer.
    if (pos+1 > len-1) {
      char* newbuf = realloc(buf, ++len * sizeof(char));
      if (newbuf == NULL) {
        free(buf);
        return NULL;        // out of memory
      } else {
        buf = newbuf;
      }
    }
    buf[pos] = c;
  }

  if (pos == 0 && c == EOF) {
    // no characters were read and we reached EOF
    free(buf);
    return NULL;
  } else {
    // pos characters were read into buf[0]..buf[pos-1].
    buf[pos] = '\0'; // terminate string
    return buf;
  }
```

> It may seem inefficient to grow the buffer by only one byte each time.
We trust `realloc` to be smart, moving the buffer to a new location (by copying it) that leaves room for growth, rather than incurring a copy of the whole string every time we call `realloc`.
It's far easier for us to leave those complexities to `realloc` than to implement them at this layer.
