# CS50 TSE Indexer
## Design Spec

According to the [Indexer Requirements Spec](REQUIREMENTS.md), the TSE *indexer* is a standalone program that reads the document files produced by the TSE crawler, builds an index, and writes that index to a file.  Its companion, the *index tester,* loads an index file produced by the indexer and saves it to another file. In this document we cover only the *indexer*.

### User interface

The indexer's only interface with the user is on the command-line; it must always have two arguments.

```
indexer pageDirectory indexFilename
```

For example, if `letters` is a pageDirectory in `../data`,

``` bash
$ indexer ../data/letters ../data/letters.index
```

### Inputs and outputs

**Input**: the indexer reads files from a directory by constructing file pathnames from the `pageDirectory` parameter followed by a numeric document ID (as described in the Requirements).

The indexer reads document files in sequential ID order, beginning at 1, until is unable to open one of those files.

**Output**: We save the index to a file using the format described in the Requirements.

### Functional decomposition into modules

We anticipate the following modules or functions:

 1. *main*, which parses arguments and initializes other modules;
 2. *indexBuild*, which builds an in-memory index from webpage files it finds in the pageDirectory;
 2. *indexPage*, which scans a webpage document to add its words to the index.

And some helper modules that provide data structures:

 1. *index*, a module providing the data structure to represent the in-memory index, and functions to read and write index files;
 1. *webpage*, a module providing the data structure to represent webpages, and to scan a webpage for words;
 2. *pagedir*, a module providing functions to load webpages from files in the pageDirectory;
 4. *word*, a module providing a function to normalize a word.

### Pseudo code for logic/algorithmic flow

The indexer will run as follows:

    parse the command line, validate parameters, initialize other modules
    call indexBuild, with pageDirectory

where *indexBuild:*

      creates a new 'index' object
      loops over document ID numbers, counting from 1
        loads a webpage from the document file 'pageDirectory/id'
        if successful, 
          passes the webpage and docID to indexPage

where *indexPage:*

     steps through each word of the webpage,
       skips trivial words (less than length 3),
       normalizes the word (converts to lower case),
       looks up the word in the index,
         adding the word to the index if needed
       increments the count of occurrences of this word in this docID

### Major data structures

The key data structure is the *index*, mapping from *word* to *(docID, #occurrences)* pairs.
The *index* is a *hashtable* keyed by *word* and storing *counters* as items.
The *counters* is keyed by *docID* and stores a count of the number of occurrences of that word in the document with that ID. 

### Testing plan

*Unit testing*.  A program *indextest* will serve as a unit test for the *index* module; it reads an index file into the internal *index* data structure, then writes the index out to a new index file.

*Integration testing*.  The *indexer*, as a complete program, will be tested by building an index from a pageDirectory, and then the resulting index will be validated by running it through the *indextest* to ensure it can be loaded.

1. Test `indexer` with various invalid arguments.
	2. no arguments
	3. one argument
	4. three or more arguments
	5. invalid `pageDirectory` (non-existent path)
	5. invalid `pageDirectory` (not a crawler directory)
	6. invalid `indexFile` (non-existent path)
	7. invalid `indexFile` (read-only directory)
	7. invalid `indexFile` (existing, read-only file)
0. Run *indexer* on a variety of pageDirectories and use *indextest* as one means of validating the resulting index.
0. Run *valgrind* on both *indexer* and *indextest* to ensure no memory leaks or errors.
