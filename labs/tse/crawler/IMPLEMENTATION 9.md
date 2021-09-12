# CS50 TSE Crawler
## Implementation Spec

In this document we reference the [Requirements Specification](REQUIREMENTS.md) and [Design Specification](DESIGN.md) and focus on the implementation-specific decisions.
The knowledge unit noted that an [implementation spec](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/design.md#implementation-spec) may include many topics; not all are relevant to the TSE or the Crawler.
Here we focus on the core subset:

-  Data structures
-  Control flow: pseudo code for overall flow, and for each of the functions
-  Detailed function prototypes and their parameters
-  Error handling and recovery
-  Testing plan

## Data structures 

We use two data structures: a 'bag' of pages that need to be crawled, and a 'hashtable' of URLs that we have seen during our crawl.
Both start empty.
The size of the hashtable (slots) is impossible to determine in advance, so we use 200.

## Control flow

The Crawler is implemented in one file `crawler.c`, with four functions.

### main

The `main` function simply calls `parseArgs` and `crawl`, then exits zero.

### parseArgs

Given arguments from the command line, extract them into the function parameters; return only if successful.

* for `seedURL`, normalize the URL and validate it is an internal URL
* for `pageDirectory`, call `pagedir_init()`
* for `maxDepth`, ensure it is an integer in specified range
* if any trouble is found, print an error to stderr and exit non-zero.

### crawl

Do the real work of crawling from `seedURL` to `maxDepth` and saving pages in `pageDirectory`.
Pseudocode:

	initialize the hashtable and add the seedURL
	initialize the bag and add a webpage representing the seedURL at depth 0
	while bag is not empty
		pull a webpage from the bag
		fetch the HTML for that webpage
		if fetch was successful,
			save the webpage to pageDirectory
			if the webpage is not at maxDepth,
				pageScan that HTML
		delete that webpage
	delete the hashtable
	delete the bag

### pageScan

This function implements the *pagescanner* mentioned in the design.
Given a `webpage`, scan the given page to extract any links (URLs), ignoring non-internal URLs; for any URL not already seen before (i.e., not in the hashtable), add the URL to both the hashtable `pages_seen` and to the bag `pages_to_crawl`.
Pseudocode:

	while there is another URL in the page
		if that URL is Internal,
			insert the webpage into the hashtable
			if that succeeded,
				create a webpage_t for it
				insert the webpage into the bag
		free the URL

## Other modules

### pagedir

We create a re-usable module `pagedir.c` to handles the *pagesaver*  mentioned in the design (writing a page to the pageDirectory), and marking it as a Crawler-produced pageDirectory (as required in the spec).
We chose to write this as a separate module, in `../common`, to encapsulate all the knowledge about how to initialize and validate a pageDirectory, and how to write and read page files, in one place... anticipating future use by the Indexer and Querier.

Pseudocode for `pagedir_init`:

	construct the pathname for the .crawler file in that directory
	open the file for writing; on error, return false.
	close the file and return true.


Pseudocode for `pagedir_save`:

	construct the pathname for the page file in pageDirectory
	open that file for writing
	print the URL
	print the depth
	print the contents of the webpage
	close the file

### libcs50

We leverage the modules of libcs50, most notably `bag`, `hashtable`, and `webpage`.
See that directory for module interfaces.
The new `webpage` module allows us to represent pages as `webpage_t` objects, to fetch a page from the Internet, and to scan a (fetched) page for URLs; in that regard, it serves as the *pagefetcher* described in the design.
Indeed, `webpage_fetch` enforces the 1-second delay for each fetch, so our crawler need not implement that part of the spec.

## Function prototypes

### crawler

Detailed descriptions of each function's interface is provided as a paragraph comment prior to each function's implementation in `crawler.c` and is not repeated here.

```c
int main(const int argc, char* argv[]);
static void parseArgs(const int argc, char* argv[],
                      char** seedURL, char** pageDirectory, int* maxDepth);
static void crawl(char* seedURL, char* pageDirectory, const int maxDepth);
static void pageScan(webpage_t* page, bag_t* pagesToCrawl, hashtable_t* pagesSeen);
```

### pagedir

Detailed descriptions of each function's interface is provided as a paragraph comment prior to each function's declaration in `pagedir.h` and is not repeated here.

```c
bool pagedir_init(const char* pageDirectory);
void pagedir_save(const webpage_t* page, const char* pageDirectory, const int docID);
```

## Error handling and recovery

All the command-line parameters are rigorously checked before any data structures are allocated or work begins; problems result in a message printed to stderr and a non-zero exit status.

Out-of-memory errors are handled by variants of the `mem_assert` functions, which result in a message printed to stderr and a non-zero exit status.
We anticipate out-of-memory errors to be rare and thus allow the program to crash (cleanly) in this way.

All code uses defensive-programming tactics to catch and exit (using variants of the `mem_assert` functions), e.g., if a function receives bad parameters.

That said, certain errors are caught and handled internally: for example, `pagedir_init` returns false if there is any trouble creating the `.crawler` file, allowing the Crawler to decide what to do; the `webpage` module returns false when URLs are not retrievable, and the Crawler does not treat that as a fatal error.

## Testing plan

Here is an implementation-specific testing plan.

### Unit testing

There are only two units (crawler and pagedir).
The crawler represents the whole system and is covered below.
The pagedir unit is tiny; it could be tested using a small C 'driver' to invoke its functions with various arguments, but it is likely sufficient to observe its behavior during the system test.

### Regression testing

The crawler can take a long time to run on some sites when `maxDepth` is more than 2.
For routine regression tests, we crawl the `letters` site at moderate depths; save the pageDirectory from one working run to compare (with `diff -r`) against future runs.

> For Lab 4, you are not required to script regression tests, though you may find the technique useful for your own testing/debugging process.

### Integration/system testing

We write a script `testing.sh` that invokes the crawler several times, with a variety of command-line arguments.
First, a sequence of invocations with erroneous arguments, testing each of the possible mistakes that can be made.
Second, a run with valgrind over a moderate-sized test case (such as `toscrape` at depth 1).
Third, runs over all three CS50 websites (`letters` at depths 0,1,2,10, `toscrape` at depths 0,1,2,3, `wikipedia` at depths 0,1,2).
Run that script with `bash -v testing.sh` so the output of crawler is intermixed with the commands used to invoke the crawler.
Verify correct behavior by studying the output, and by sampling the files created in the respective pageDirectories.

> For Lab 4, as noted in the assignment, you may submit a smaller test run.
> Furthermore, we recommend turning off detailed logging output for these tests, as they make `testing.out` rather large!
