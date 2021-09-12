Our Tiny Search Engine (TSE) is inspired by the material in the paper *[Searching the Web](media/searchingtheweb.pdf)*, as cited in the [unit](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/read-search.md).

## TSE Design

The overall architecture presented below shows the modular decomposition of the system:

![Tiny Search Engine modular design](media/crawler/designandcrawler1x.png)

------------------------------------------------------------------------

Our Tiny Search Engine (TSE) design consists of three subsystems:


1. The **crawler** crawls a website and retrieves webpages starting with a specified URL.
	It parses the initial webpage, extracts any embedded URLs and retrieves those pages, and crawls the pages found at those URLs, but limiting itself to some threshold number of hops from the seed URL, and avoiding visiting any given URL more than once.
	It saves the pages, and the URL and depth for each, in files.
	When the crawler process is complete, the indexing of the collected documents can begin.

2. The **indexer** extracts all the keywords for each stored webpage and creates a lookup table that maps each word found to all the documents (webpages) where the word was found.
   It saves this table in a file.

3. The **query engine** responds to requests (queries) from users.
	The *query processor module* loads the index file and searches for pages that include the search keywords.
	Because there may be many hits, we need a *ranking module* to rank the results (e.g., high to low number of instances of a keyword on a page).

Each subsystem is a standalone program executed from the command line, but they inter-connect through files in the file system.

We'll look deeper at the requirements for the indexer and querier later.
Right now, let's focus on the crawler.

## <a id="crawler-output">TSE Crawler</a>

We provide the specific Crawler specs in [Lab4](https://github.com/cs50dartmouth21FS1/home/blob/main/labs/tse/crawler).

Below is the output of our crawler when the program crawls one of our simple test websites to a maximum depth of 10 (though it reaches all pages within this website in only five hops from the seed).
The crawler prints status information as it goes along.

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f01b379a-fcf3-491c-ae78-ad1400f5ff64)**

```
$ crawler/crawler http://cs50tse.cs.dartmouth.edu/tse/letters/index.html data/letters-3 10
 0   Fetched: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 0  Scanning: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 0     Found: http://cs50tse.cs.dartmouth.edu/tse/letters/A.html
 0     Added: http://cs50tse.cs.dartmouth.edu/tse/letters/A.html
 1    Fetched: http://cs50tse.cs.dartmouth.edu/tse/letters/A.html
 1   Scanning: http://cs50tse.cs.dartmouth.edu/tse/letters/A.html
 1      Found: https://en.wikipedia.org/wiki/Algorithm
 1   IgnExtrn: https://en.wikipedia.org/wiki/Algorithm
 1      Found: http://cs50tse.cs.dartmouth.edu/tse/letters/B.html
 1      Added: http://cs50tse.cs.dartmouth.edu/tse/letters/B.html
 1      Found: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 1    IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 2     Fetched: http://cs50tse.cs.dartmouth.edu/tse/letters/B.html
 2    Scanning: http://cs50tse.cs.dartmouth.edu/tse/letters/B.html
 2       Found: https://en.wikipedia.org/wiki/Breadth-first_search
 2    IgnExtrn: https://en.wikipedia.org/wiki/Breadth-first_search
 2       Found: http://cs50tse.cs.dartmouth.edu/tse/letters/C.html
 2       Added: http://cs50tse.cs.dartmouth.edu/tse/letters/C.html
 2       Found: http://cs50tse.cs.dartmouth.edu/tse/letters/D.html
 2       Added: http://cs50tse.cs.dartmouth.edu/tse/letters/D.html
 2       Found: http://cs50tse.cs.dartmouth.edu/tse/letters/E.html
 2       Added: http://cs50tse.cs.dartmouth.edu/tse/letters/E.html
 2       Found: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 2     IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 3      Fetched: http://cs50tse.cs.dartmouth.edu/tse/letters/E.html
 3     Scanning: http://cs50tse.cs.dartmouth.edu/tse/letters/E.html
 3        Found: https://en.wikipedia.org/wiki/ENIAC
 3     IgnExtrn: https://en.wikipedia.org/wiki/ENIAC
 3        Found: http://cs50tse.cs.dartmouth.edu/tse/letters/F.html
 3        Added: http://cs50tse.cs.dartmouth.edu/tse/letters/F.html
 3        Found: http://cs50tse.cs.dartmouth.edu/tse/letters/G.html
 3        Added: http://cs50tse.cs.dartmouth.edu/tse/letters/G.html
 3        Found: http://cs50tse.cs.dartmouth.edu/tse/letters/A.html
 3      IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/A.html
 3        Found: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 3      IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 4       Fetched: http://cs50tse.cs.dartmouth.edu/tse/letters/G.html
 4      Scanning: http://cs50tse.cs.dartmouth.edu/tse/letters/G.html
 4         Found: https://en.wikipedia.org/wiki/Graph_traversal
 4      IgnExtrn: https://en.wikipedia.org/wiki/Graph_traversal
 4         Found: http://cs50tse.cs.dartmouth.edu/tse/letters/H.html
 4         Added: http://cs50tse.cs.dartmouth.edu/tse/letters/H.html
 4         Found: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 4       IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 5        Fetched: http://cs50tse.cs.dartmouth.edu/tse/letters/H.html
 5       Scanning: http://cs50tse.cs.dartmouth.edu/tse/letters/H.html
 5          Found: https://en.wikipedia.org/wiki/Huffman_coding
 5       IgnExtrn: https://en.wikipedia.org/wiki/Huffman_coding
 5          Found: http://cs50tse.cs.dartmouth.edu/tse/letters/B.html
 5        IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/B.html
 5          Found: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 5        IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 4       Fetched: http://cs50tse.cs.dartmouth.edu/tse/letters/F.html
 4      Scanning: http://cs50tse.cs.dartmouth.edu/tse/letters/F.html
 4         Found: https://en.wikipedia.org/wiki/Fast_Fourier_transform
 4      IgnExtrn: https://en.wikipedia.org/wiki/Fast_Fourier_transform
 4         Found: http://cs50tse.cs.dartmouth.edu/tse/letters/H.html
 4       IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/H.html
 4         Found: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 4       IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 3      Fetched: http://cs50tse.cs.dartmouth.edu/tse/letters/D.html
 3     Scanning: http://cs50tse.cs.dartmouth.edu/tse/letters/D.html
 3        Found: https://en.wikipedia.org/wiki/Depth-first_search
 3     IgnExtrn: https://en.wikipedia.org/wiki/Depth-first_search
 3        Found: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 3      IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 3      Fetched: http://cs50tse.cs.dartmouth.edu/tse/letters/C.html
 3     Scanning: http://cs50tse.cs.dartmouth.edu/tse/letters/C.html
 3        Found: https://en.wikipedia.org/wiki/Computational_biology
 3     IgnExtrn: https://en.wikipedia.org/wiki/Computational_biology
 3        Found: http://cs50tse.cs.dartmouth.edu/tse/letters/D.html
 3      IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/D.html
 3        Found: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
 3      IgnDupl: http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
$ 
```

For each URL crawled the program creates a file and places in the file the URL and filename followed by all the contents of the webpage.
Below is a peek at the files created during the above crawl.
Notice how each page starts with the URL, then a number (the depth of that page during the crawl), then the contents of the page (here I printed only the first line of the content).

```
$ cd data/letters-3/
$ ls
1  10  2  3  4  5  6  7  8  9
$ head -3 1
http://cs50tse.cs.dartmouth.edu/tse/letters/index.html
0
<html>
$ head -3 2
http://cs50tse.cs.dartmouth.edu/tse/letters/A.html
1
<html>
$ head -3 10
http://cs50tse.cs.dartmouth.edu/tse/letters/C.html
3
<html>
$ 
```

## Organization of the TSE code

Let's take a look at the structure of our TSE solution - so you can see what you're aiming for.
Our TSE comprises six subdirectories:

* **libcs50** - a library of code we provide
* **common** - a library of code you write
* **crawler** - the crawler
* **indexer** - the indexer
* **querier** - the querier
* **data** - with subdirectories where the crawler and indexer can write files, and the querier can read files.

My top-level `.gitignore` file excludes `data` from the repository (in addition to the standard CS50 exclusion patterns), because the data files are big, changing often, and don't deserve to be saved.
*Please don't commit data files to your repo, either.*

My full tree looks like this; your implementation may have slightly different files related to testing.

```
.
|-- .gitignore
|-- Makefile
|-- README.md
|-- common
|   |-- Makefile
|   |-- index.c
|   |-- index.h
|   |-- pagedir.c
|   |-- pagedir.h
|   |-- word.c
|   `-- word.h
|-- crawler
|   |-- .gitignore
|   |-- DESIGN.md
|   |-- IMPLEMENTATION.md
|   |-- Makefile
|   |-- README.md
|   |-- REQUIREMENTS.md
|   |-- crawler.c
|   |-- testing.out
|   |-- testing.sh
|   `-- valgrind.sh
|-- data
|-- indexer
|   |-- .gitignore
|   |-- DESIGN.md
|   |-- IMPLEMENTATION.md
|   |-- Makefile
|   |-- README.md
|   |-- REQUIREMENTS.md
|   |-- indexer.c
|   |-- indextest.c
|   |-- testing.out
|   `-- testing.sh
|-- libcs50
|   |-- Makefile
|   |-- README.md
|   |-- bag.c
|   |-- bag.h
|   |-- counters.c
|   |-- counters.h
|   |-- file.c
|   |-- file.h
|   |-- hash.c
|   |-- hash.h
|   |-- hashtable.c
|   |-- hashtable.h
|   |-- libcs50-given.a
|   |-- mem.c
|   |-- mem.h
|   |-- set.c
|   |-- set.h
|   |-- webpage.c
|   `-- webpage.h
`-- querier
    |-- .gitignore
    |-- DESIGN.md
    |-- IMPLEMENTATION.md
    |-- Makefile
    |-- README.md
    |-- REQUIREMENTS.md
    |-- fuzzquery.c
    |-- querier.c
|   |-- testing.out
    |-- testing.sh
    `-- testing.txt
```

Our crawler, indexer, and querier each consist of just one `.c` file.
They share some common code, which we keep in the `common` directory:

* **pagedir** - a suite of functions to help the crawler write pages to the pageDirectory and help the indexer read them back in
* **index** - a suite of functions that implement the "index" data structure; this module includes functions to write an index to a file (used by indexer) and read an index from a file (used by querier).
* **word** - a function `normalizeWord` used by both the indexer and the querier.

Each of the program directories (crawler, indexer, querier) include a few files related to testing, as well.

You'll recognize the Lab3 data structures - they're all in the `libcs50` library.
(Note the flatter organization - there's not a separate subdirectory (with Makefile or test code) for each data structure.)

## Summary

What are some of the key ideas in the crawler design?

First, it ***separates application-specific logic from general-purpose utility modules.***
By leveraging general-purpose modules from Lab 3, coding the crawler-specfic logic is much cleaner and simpler than if it were woven in with the data-structure code.

Code that weaves the "business logic" of an application into the details of data structures is likely more complicated, more buggy, harder to debug, harder to test, harder to maintain, and much harder to reuse and extend.

Second, we note ***good data-structure design is key to successful projects.***
The design of the crawler remains simple because we chose two data structures – a *bag* for the set of pages yet to crawl, and a *hashtable* for the set of URLs already seen – that support the overall control flow.

For the TSE we've anticipated many design and implementation decisions for you – based on years of experience with this project.
In a real project, however, developers often find a need to go back and refine early modules to provide different functionality, or to factor out code that needs to be used by multiple components.
(As did we, when we first wrote this TSE implementation!)
It's hard to have perfect foresight.

[Pragmatic Programmer Tip](https://pragprog.com/tips/):

> **There are no final decisions:**
>  No decision is cast in stone.
> Instead, consider each as being written in the sand at the beach, and plan for change.

The software development process is precisely that: a process.
It is not a programmer on a one-way street, but more like a chef crafting a new dish.
You have a plan that everyone agrees on, you begin to execute on that plan, and sometimes conditions change and unexpected interactions arise.
You have to handle them and and still get to the goal.
