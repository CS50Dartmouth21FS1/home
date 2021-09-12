In a recent unit we talked about *unit testing*, and the difference between *glass-box testing* and *black-box testing*.
Usually, these tests are based on a carefully constructed series of test cases, devised to test all code sequences and push on the "edge cases".

However, such tests are only as good as the test writer - who must study the code (for glass-box testing) or the specs (for black-box testing) to think of the suitable test cases.
It's possible they will miss some important cases.

Another solution, therefore, is ***fuzz testing***, a form of black-box testing in which you fire thousands of random inputs at the program to see how it reacts.
The chances of triggering an unconsidered test case is far greater if you try a lot of cases!

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=acab720d-bf47-4465-b442-ad18017b5507)**

We provide `fuzzquery.c`, a fuzz-testing program, for testing a querier.
It generates a series of random queries on stdout, which it then pipes to the querier on stdin.
Here's the core of the fuzz tester:

```c
/**************** generateQuery ****************/
/* generate one random query and print to stdout.
 * pull random words from the wordlist and from the dictionary.
 */
static void
generateQuery(const wordlist_t* wordlist, const wordlist_t* dictionary)
{
  // some parameters that affect query generation
  const int maxWords = 6;            // generate 1..maxWords
  const float orProbability = 0.3;   // P(OR between two words)
  const float andProbability = 0.2;  // P(AND between two words)
  const float dictProbability = 0.2; // P(draw from dict instead of wordlist)

  int qwords = rand() % maxWords + 1; // number of words in query
  for (int qw = 0; qw < qwords; qw++) {
    // draw a word either dictionary or wordlist
    if ((rand() % 100) < (dictProbability * 100)) {
      printf("%s ", dictionary->words[rand() % dictionary->nWords]);
    } else {
      printf("%s ", wordlist->words[rand() % wordlist->nWords]);
    }

    // last word?
    if (qw < qwords-1) {
      // which operator to print?
      int op = rand() % 100;
      if (op < (andProbability * 100)) {
        printf("AND ");
      }
      else if (op < (andProbability * 100 + orProbability * 100)) {
        printf("OR ");
      }
    }
  }
  printf("\n");
}
```

With the following setup,

```bash
cd tse
seed="http://cs50tse.cs.dartmouth.edu/tse/toscrape/index.html"
pdir="data/toscrape-2"
indx="data/toscrape-2.index"
mkdir $pdir
crawler/crawler $seed $pdir 2
indx="data/toscrape-2.index"
```

And here's the output of 10 random queries:

```bash
$ querier/fuzzquery $indx 10 0
querier/fuzzquery: generating 10 queries from 13563 words
inthe AND quarters 
hangs OR visited kahneman OR beneath shopping 
nationally holery OR predicts 
answers axell conduct OR christine OR Mississippians OR sorbonne 
endowment OR cosmic lover sketchbook AND priest OR bfed 
orientation iceland describe worse OR defeating 
clerks 
arnold streusel OR braved 
multiplatform 
patience OR nightstruck OR bowerbird AND antoinette AND stances 
$
```

And here's what happens when we pipe it to our querier:

```
$ querier/fuzzquery $indx 10 0 | querier/querier $pdir $indx
querier/fuzzquery: generating 10 queries from 13563 words
Query: inthe and quarters 
No documents match.
-----------------------------------------------
Query: hangs or visited kahneman or beneath shopping 
Matches 3 documents (ranked):
score   1 doc 171: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/night-shift-night-shift-1-20_335/index.html
score   1 doc 536: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/the-last-painting-of-sara-de-vos_259/index.html
score   1 doc 569: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/the-last-mile-amos-decker-2_754/index.html
-----------------------------------------------
Query: nationally holery or predicts 
Matches 1 documents (ranked):
score   1 doc 246: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/the-grand-design_405/index.html
-----------------------------------------------
Query: answers axell conduct or christine or mississippians or sorbonne 
Matches 2 documents (ranked):
score   2 doc 357: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/a-piece-of-sky-a-grain-of-rice-a-memoir-in-four-meditations_878/index.html
score   1 doc 367: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/orchestra-of-exiles-the-story-of-bronislaw-huberman-the-israel-philharmonic-and-the-one-thousand-jews-he-saved-from-nazi-horrors_337/index.html
-----------------------------------------------
Query: endowment or cosmic lover sketchbook and priest or bfed 
Matches 2 documents (ranked):
score   1 doc  28: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/olio_984/index.html
score   1 doc  20: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/black-dust_976/index.html
-----------------------------------------------
Query: orientation iceland describe worse or defeating 
Matches 1 documents (ranked):
score   2 doc 499: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/beowulf_126/index.html
-----------------------------------------------
Query: clerks 
Matches 1 documents (ranked):
score   1 doc 157: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/a-distant-mirror-the-calamitous-14th-century_652/index.html
-----------------------------------------------
Query: arnold streusel or braved 
Matches 1 documents (ranked):
score   1 doc 150: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/the-mathews-men-seven-brothers-and-the-war-against-hitlers-u-boats_408/index.html
-----------------------------------------------
Query: multiplatform 
Matches 1 documents (ranked):
score   1 doc 204: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/m-train_598/index.html
-----------------------------------------------
Query: patience or nightstruck or bowerbird and antoinette and stances 
Matches 9 documents (ranked):
score   5 doc 524: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/patience_916/index.html
score   1 doc 511: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/sequential-art_5/index.html
score   1 doc 518: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/i-hate-fairyland-vol-1-madly-ever-after-i-hate-fairyland-compilations-1-5_899/index.html
score   1 doc 519: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/lumberjanes-vol-3-a-terrible-plan-lumberjanes-9-12_905/index.html
score   1 doc 520: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/lumberjanes-vol-1-beware-the-kitten-holy-lumberjanes-1-4_906/index.html
score   1 doc 521: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/lumberjanes-vol-2-friendship-to-the-max-lumberjanes-5-8_907/index.html
score   1 doc 522: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/orange-the-complete-collection-1-orange-the-complete-collection-1_914/index.html
score   1 doc 523: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/outcast-vol-1-a-darkness-surrounds-him-outcast-1_915/index.html
score   1 doc 255: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/young-adult_21/page-2.html
-----------------------------------------------
```

We could generate a different series of random queries by changing the random seed, and we can run a lot more queries, too!

```
$ querier/fuzzquery $indx 10 999 | querier/querier $pdir $indx > /dev/null
querier/fuzzquery: generating 10 queries from 13563 words
$ querier/fuzzquery $indx 10000 9999 | querier/querier $pdir $indx > /dev/null
querier/fuzzquery: generating 10000 queries from 13563 words
```

The fuzz tester does not test *all* aspects of the querier; in particular, it will not generate syntactically incorrect inputs.
Those should be tested by another program, perhaps another fuzz tester.
Furthermore, it does not verify whether the querier actually produces the right answers!

For regression testing, we might save the querier output in a file, and then compare the output of a fresh test run against the saved results from earlier runs.
If we had earlier believed those results to be correct, then seeing unchanged output would presumably indicate the results (and thus the new code) are still correct.
