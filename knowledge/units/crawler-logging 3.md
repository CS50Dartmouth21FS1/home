In the [demo output of our crawler](#crawler-output), notice how it printed the depth of the current crawl at left, then indented slightly based on the current depth, then printed a single word meant to indicate what is being done, then printed the URL.
By ensuring a consistent format, and choosing a simple/unique word for each type of line, we can post-process the output with `grep`, `awk`, and so forth, enabling us to run various checks on the output of the crawler.
(Much better than a mish-mash of arbitrary output formats!)
The code `IgnDupl` meant the crawler was ignoring a duplicate URL (one it had seen before), and `IgnExtrn` meant the crawler was ignoring an 'external' URL (because we limit CS50 crawlers to our webserver to avoid causing trouble on any real webservers).

All of this 'logging' output was produced by a simple function:

```c
// log one word (1-9 chars) about a given url                                   
static void logr(const char *word, const int depth, const char *url)
{
  printf("%2d %*s%9s: %s\n", depth, depth, "", word, url);
}
```

The program thus has just one `printf` call; if we want to tweak the format, we just need to edit one line and not every log-type `printf` in the code.

Actually, the code is like this:

```c
// log one word (1-9 chars) about a given url                                   
static void logr(const char *word, const int depth, const char *url)
{
#ifdef APPTEST
  printf("%2d %*s%9s: %s\n", depth, depth, "", word, url);
#else
  ;
#endif
}
```

Notice the `#ifdef` block that can be triggered by a compile-time switch.
See the [unit](https://github.com/CS50Dartmouth21FS1/home/blob/main/knowledge/units/c-conditional-compilation.md) about this trick.

Anyway, at strategic points in the code, there is a call like this one:

```c
logr("Fetched", page->depth, page->url);
```

Such code is compact, readable, maintainable, and can be entirely flipped on and off with one `#ifdef`.
