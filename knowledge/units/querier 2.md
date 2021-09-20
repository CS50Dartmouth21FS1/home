The third component of the Tiny Search Engine is the *Querier*, which reads the index produced by the Indexer and the page files produced by the Crawler, to interactively answer written queries entered by the user.

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c9ddf20a-f3a5-4323-945a-ad1801785c84)**

Our Querier loads the index into memory (a data structure we developed for the Indexer) and then prompts the user for queries.
Queries are comprised of words, with optional `and`/`or` operators.
For example,

```
computer science
computer and science
computer or science
baseball or basketball or ultimate frisbee
```

The first two examples are treated identically, matching only documents that have *both* words - not necessarily together (as in the phrase "computer science").
The third picks up documents that have *either* word.
The fourth matches documents that mention baseball, or basketball, or both "ultimate" and the word "frisbee" (not necessarily together).

Here's an example run, with the output truncated a bit:

```
$./querier ~/shared/tse-output/crawler/crawler.data/data22 ~/shared/tse-output/indexer/indexer.data
Query? Europe travel
Query: europe travel
Matches 1 documents (ranked):
score	1 doc  80: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/travel_2/index.html
-----------------------------------------------
Query? Europe and travel
Query: europe and travel
Matches 1 documents (ranked):
score	1 doc  80: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/travel_2/index.html
-----------------------------------------------
Query? Europe or travel
Query: europe or travel
Matches 56 documents (ranked):
score	5 doc  80: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/travel_2/index.html
score	3 doc	8: http://cs50tse.cs.dartmouth.edu/tse/wikipedia/Computer_science.html
score	1 doc	9: http://cs50tse.cs.dartmouth.edu/tse/toscrape/
score	1 doc  10: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/page-2.html
score	1 doc  11: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/its-only-the-himalayas_981/index.html
score	1 doc  31: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/crime_51/index.html
score	1 doc  32: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/erotica_50/index.html
score	1 doc  33: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/cultural_49/index.html
score	1 doc  34: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/politics_48/index.html
score	1 doc  35: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/health_47/index.html
score	1 doc  36: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/novels_46/index.html
score	1 doc  37: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/short-stories_45/index.html
score	1 doc  38: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/suspense_44/index.html
score	1 doc  39: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/christian_43/index.html
score	1 doc  40: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/historical_42/index.html
score	1 doc  41: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/self-help_41/index.html
score	1 doc  42: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/academic_40/index.html
score	1 doc  43: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/spirituality_39/index.html
score	1 doc  44: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/contemporary_38/index.html
score	1 doc  45: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/thriller_37/index.html
score	1 doc  46: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/biography_36/index.html
score	1 doc  47: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/business_35/index.html
score	1 doc  48: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/christian-fiction_34/index.html
score	1 doc  49: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/food-and-drink_33/index.html
score	1 doc  50: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/history_32/index.html
score	1 doc  51: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/horror_31/index.html
score	1 doc  52: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/humor_30/index.html
score	1 doc  53: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/adult-fiction_29/index.html
score	1 doc  54: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/parenting_28/index.html
score	1 doc  55: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/autobiography_27/index.html
score	1 doc  56: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/psychology_26/index.html
score	1 doc  57: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/art_25/index.html
score	1 doc  58: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/paranormal_24/index.html
score	1 doc  59: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/poetry_23/index.html
score	1 doc  60: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/science_22/index.html
score	1 doc  61: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/young-adult_21/index.html
score	1 doc  62: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/new-adult_20/index.html
score	1 doc  63: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/fantasy_19/index.html
score	1 doc  64: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/add-a-comment_18/index.html
score	1 doc  65: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/sports-and-games_17/index.html
score	1 doc  66: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/science-fiction_16/index.html
score	1 doc  67: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/default_15/index.html
score	1 doc  68: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/music_14/index.html
score	1 doc  69: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/nonfiction_13/index.html
score	1 doc  70: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/religion_12/index.html
score	1 doc  71: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/childrens_11/index.html
score	1 doc  72: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/fiction_10/index.html
score	1 doc  73: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/womens-fiction_9/index.html
score	1 doc  74: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/romance_8/index.html
score	1 doc  75: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/philosophy_7/index.html
score	1 doc  76: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/classics_6/index.html
score	1 doc  77: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/sequential-art_5/index.html
score	1 doc  78: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/historical-fiction_4/index.html
score	1 doc  79: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/mystery_3/index.html
score	1 doc  81: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books_1/index.html
score	1 doc  82: http://cs50tse.cs.dartmouth.edu/tse/toscrape/index.html
-----------------------------------------------
Query? Tiny Search Engine
Query: tiny search engine
No documents match.
-----------------------------------------------
Query? TSE
Query: tse
Matches 2 documents (ranked):
score	2 doc	1: http://cs50tse.cs.dartmouth.edu/tse/
score	1 doc  83: http://cs50tse.cs.dartmouth.edu/tse/letters/
-----------------------------------------------
Query? git-flow
Error: bad character '-' in query.
Query? and dartmouth harvard
Query: and dartmouth harvard
Error: 'and' cannot be first
Query?	  spaces   do  not  matter
Query: spaces do not matter
No documents match.
-----------------------------------------------
Query? exit
Query: exit
Matches 3 documents (ranked):
score	2 doc	6: http://cs50tse.cs.dartmouth.edu/tse/wikipedia/Unix.html
score	1 doc	7: http://cs50tse.cs.dartmouth.edu/tse/wikipedia/C_(programming_language).html
score	1 doc  59: http://cs50tse.cs.dartmouth.edu/tse/toscrape/catalogue/category/books/poetry_23/index.html
-----------------------------------------------
Query? ^D
$ 
```

You can see the querier prompts `Query?` and then echos the pre-processed query before computing the matches.
Preprocessing strips leading and trailing spaces, compacts internal spaces, and lowercases all words.
