Take a look at the Requirements Spec and Design Spec for the Indexer, which you can find detailed in [Lab5](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/labs/tse/indexer).
The Indexer is required to read the documents in the `pageDirectory` output by the Crawler, build an index mapping from words to documents, and write that index to a file.
(Later, the Querier will read the index and use it to answer queries.)

Here we focus on the design.
The key question when designing the Indexer is this: what data structure do we use to represent the inverted index?

> It's called an *inverted* index because it maps from words to documents, the opposite of a document (which is, in effect, a mapping of documents to words).

A hashtable is a great start, because we can look up a word in a hashtable in O(1) time.
But what is in the hashtable?
For each word, we need a list of documents.
(Not the documents themselves, silly, just the document IDs.)
Actually, to enable us to later *rank* the matches, we'd also like to record how many times the given word appears in each document.

Consider the data structures we have handy: hashtable, set, bag, and counters.
(Oh, and tree.)
A *hashtable of countersets* is perfect to map from a word to a list of (docID, count) pairs.
See the diagram below:

![Indexer data flow](media/indexer/data-model.png)

We get to use three out of our four data structures: hashtable, set, and counters!

When processing a given document (identified by docID), consider each word; look up that word in the hashtable and retrieve a pointer to a `counters_t*` item; then use `counters_add()` on that item, using the docID as a key.

Your data-set iterators – like `hashtable_iterate()` – should be very useful for saving an index to a file, or for later loading an index from a file.

> Now is a good time to read Section 4 in *[Searching the Web](media/searchingtheweb.pdf)*, the paper about search engines.

## Indexer demo

**[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=a36d11ab-5263-4904-9520-ad16017c24fa)** of our Indexer in action.

