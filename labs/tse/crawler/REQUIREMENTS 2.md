# CS50 TSE Crawler
## Requirements Spec

> In a requirements spec, **shall do** means **must do**.

The TSE crawler is a standalone program that crawls the web and retrieves webpages starting from a "seed" URL.
It parses the seed webpage, extracts any embedded URLs, then retrieves each of those pages, recursively, but limiting its exploration to a given "depth".

The crawler **shall**:

1. execute from a command line with usage syntax `./crawler seedURL pageDirectory maxDepth`
    * where `seedURL` is an 'internal' directory, to be used as the initial URL,
    * where `pageDirectory` is the (existing) directory in which to write downloaded webpages, and
    * where `maxDepth` is an integer in range [0..10] indicating the maximum crawl depth.
1. mark the `pageDirectory` as a 'directory produced by the Crawler' by creating a file named `.crawler` in that directory.
1. crawl all "internal" pages reachable from `seedURL`, following links to a maximum depth of `maxDepth`; where `maxDepth=0` means that crawler only explores the page at `seedURL`, and `maxDepth=1` means that crawler only explores the page at `seedURL` and those pages to which `seedURL` links, and so forth inductively.
1. print nothing to stdout, other than logging its progress; see an example format in the [knowledge unit](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/crawler.md).
 Write each explored page to the `pageDirectory` with a unique document ID, wherein
   * the document `id` starts at 1 and increments by 1 for each new page,
   * and the filename is of form `pageDirectory/id`,
   * and the first line of the file is the URL,
   * and the second line of the file is the depth,
   * and the rest of the file is the page content (the HTML, unchanged).
1. exit zero if successful; exit with an error message to stderr and non-zero exit status if it encounters an unrecoverable error, including
	* out of memory
	* invalid number of command-line arguments
	* `seedURL` is invalid or not internal
	* `maxDepth` is invalid or out of range
	* unable to create a file of form `pageDirectory/.crawler`
	* unable to create or write to a file of form `pageDirectory/id`


**Definition**:
A *normalized URL* is the result of passing a URL through `normalizeURL()`; see the documentation of that function.
An *Internal URL* is a URL that, when normalized, begins with `http://cs50tse.cs.dartmouth.edu/tse/`.

One example:
`Http://CS50TSE.CS.Dartmouth.edu//index.html`
becomes
`http://cs50tse.cs.dartmouth.edu/index.html`.


**Assumption**:
The `pageDirectory` does not contain any files whose name is an integer (i.e., `1`, `2`, ...).

**Limitation**:
The Crawler shall pause at least one second between page fetches, and shall ignore non-internal and non-normalizable URLs.
(The purpose is to avoid overloading our webserver and to avoid causing trouble on any webservers other than the CS50 test server.)
