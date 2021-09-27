In this unit, we will learn about *Iterators* and their uses:

 * what is an Iterator, and why to use an Iterator
 * how to code an Iterator in the context of our data structures
 * how it leverages function pointers in C
 * passing arguments to iterators
 * several uses of iterators
 * using iterators to merge sets

## Why iterators?

*Iterators* are a powerful concept, especially when writing an abstract data structure that represents a collection.
We have seen several such data structures - tree, bag, set, hashtable, and counters.
If the collection implements an *iterator*, we can apply some function to every item in that collection...
e.g., for printing, counting, modifying, and even combining multiple collections.

In Lab 3 we provide an `_iterate()` method in the `bag` module and you add a similar method for your `set`, `hashtable`, and `counters` modules.

In Lab 5 these iterators are helpful in writing the contents of the Index to the index file.

In Lab 6 these iterators are helpful in combining the set of matches for one word with the set of matches for another word.

## Coding an iterator

Recall the [bag module](https://github.com/CS50Dartmouth21FS1/examples/blob/main/bag-unit/bag.c), which has a `bag_iterate()` method:

```c
void
bag_iterate(bag_t* bag, void* arg, void (*itemfunc)(void* arg, void* item) )
{
  if (bag != NULL && itemfunc != NULL) {
    // call itemfunc with arg, on each item
    for (bagnode_t* node = bag->head; node != NULL; node = node->next) {
      (*itemfunc)(arg, node->item); 
    }
  }
}
```

Notice that the code begins with defensive programming - in case the caller accidentally calls it with NULL parameters.

Otherwise, the function is a simple `for` loop, stepping through each item in the bag.
This iterator makes no promise about the *order* in which it processes items; after all, a 'bag' is an unordered, unlabeled collection of 'things'.

## Function pointers

Recall our discussion of *function pointers* from an earlier [unit](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/modules.md).
The second parameter to `bag_iterate` declares `itemfunc` as a *pointer to a function* that itself takes two parameters: an `arg` and a `data`.
Both are void pointers, that is, pointers to *some unspecified type*.
Because the iterator receives a function pointer from its caller, and the arg/data parameters are arbitrary pointers, this iterator can work on items of any type, and compute any sort of function on those items, making it truly general-purpose.

Look inside the `for` loop, where we call `itemfunc`.
Here, we dereference the *function pointer* to get a *function*, then call it with two parameters: the `arg` provided to us, and the data for this item.
(Syntactically, we have to wrap the dereference in parentheses, but otherwise, it's just like any other function call.)

## Arguments

Sometimes, though not always, the caller will need a way of communicating other information to the `itemfunc` - not just the information about the item our iterator can provide.
Thus, the iterator takes `arg`, a pointer to arbitrary something, and passes it right on through to the `itemfunc`.
This mechanism is general-purpose:

 * pass `arg=NULL` if the caller has no need to send additional arguments to its `itemfunc`;
 * pass a pointer to a simple variable if the caller just needs to get information into the `itemfunc`;
 * indeed, in that case, the variable is passed *by reference* and thus the `itemfunc` can update the variable if needed;
 * furthermore, if the caller needs to send multiple things to the `itemfunc`, it can pass a pointer to a `struct` holding those things.

Let's look at some examples.

## Examples with iterators

***Printing.***
First, let's suppose we don't have (or don't like) the `set_print()` method, which tends to print some text/formatting around each item.
Our `settest.c` uses the iterator to print just the keys:

```c
...
  printf("\nSimpleprint:\n");
  set_iterate(set, stdout, simpleprint);
  printf("\n");
...

/* print the given item to the given file;
 * just print the key
 */
static void 
simpleprint(void* arg, const char* key, void* item)
{
  FILE* fp = arg;
  if (fp != NULL) {
    fprintf(fp, "%s ", key);
  }
}
```

Here, we pass the file pointer through the `arg` parameter.
Notice how `simpleprint` immediately copies its `arg` parameter into a local variable of the right type.
In this case, `simpleprint` ignores the `item` pointer; that's fine.

***Counting.***
Even simpler, we could just count the items.
But where do we put the counter?
Define a local variable and pass its address as the `arg` as seen in `bagtest.c`:

```c
  printf("\nCount (should be %d): ", namecount);
  bagcount = 0;
  bag_iterate(bag1, &bagcount, itemcount);
  printf("%d\n", bagcount);
...

/* count the non-null items in the bag.
 * note here we don't care what kind of item is in bag.
 */
static void itemcount(void* arg, void* item)
{
  int* nitems = arg;

  if (nitems != NULL && item != NULL)
    (*nitems)++;
}
```

***Multiple arguments.***
What if we want two counters?
In my version of `settest` I read in weather reports, storing a `struct weather` as the item for each weather station; that struct includes a member `tempF` representing the current temperature in Fahrenheit.
Let's write an iterator to count the number of hot spots and cold spots.

```c
// a little structure to carry two counters.
struct hotcold {
  int hot;
  int cold;
};
...

  printf("\nSummary: ");
  struct hotcold counts = {0, 0};
  set_iterate(set, &counts, tempcount);
  printf("%d hot, %d cold\n", counts.hot, counts.cold);
...


static void 
tempcount(void* arg, const char* key, void* item)
{
  if (arg != NULL && item != NULL) {
    struct hotcold* ctrs = arg;
    struct weather* wp = item;
    if (wp->tempF > 80) ctrs->hot++;
    if (wp->tempF < 32) ctrs->cold++;
  }
}
```
Once the stucture is defined, our use of the iterator is much like the previous example.
We define and initialize a local variable, pass a pointer to that variable as our `arg`, and then the `itemfunc` copies that pointer into a pointer of the relevant type so it can access (and update) the contents.
*Notice that it is not necessary to `malloc` space in order to pass a pointer to the iterator* - in this example, `&counts` is a pointer to a local variable.
That variable just happens to be a `struct` instead of an `int`.

## Merging sets with set_iterate()

The 'bag' module is nice because it is very simple, but the 'set' module allows us to do more interesting things when the items have a key as well.
Let's look at some more complex examples.

***Merging two sets.***
We'll start with a simple case.
Here the sets represent schools, where the `key` is the name of the school.
See [set_iterate1](https://github.com/CS50Dartmouth21FS1/examples/blob/main/set_iterate1.c).
(Note also [set_iterate.makefile](https://github.com/CS50Dartmouth21FS1/examples/blob/main/set_iterate.makefile).)

```c
  set_t* setA;
  set_t* setB;
  set_t* result;
... initialize each set with set_new
... fill setA and setB with set_insert

  printf("\nMerge of setA into result: \n");
  set_merge(result, setA);
  set_print(result, stdout, itemprint);
  putchar('\n');

  printf("\nMerge of setB into result: \n");
  set_merge(result, setB);
  set_print(result, stdout, itemprint);
  putchar('\n');
...

/* Merge the second set into the first set;
 * the second set is unchanged.
 */
static void 
set_merge(set_t* setA, set_t* setB)
{
  set_iterate(setB, setA, set_merge_helper);
}

/* Consider one item for insertion into the other set.
 */
static void 
set_merge_helper(void* arg, const char* key, void* item)
{
  set_t* setA = arg;

  if (set_insert(setA, key, item))
    printf("\t%s added\n", key);
  else
    printf("\t%s exists\n", key);

}
```

Notice how the above approach iterates over one set (setB) and, for each item in that set, tries to insert or update its value in the first set (setA).
At the end, setB is unchanged but setA should have all items from both sets.

**[:arrow_forward: Video demo of the above](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=2cfcc51f-25ac-4fb8-8453-ad1601805e41)**


***Merging two sets and their data.***
Now a more interesting case, in which the set items each hold data we want to combine.
In this simple test, that datum is just an integer - actually, a pointer to an integer.
See [set_iterate2](https://github.com/CS50Dartmouth21FS1/examples/blob/main/set_iterate2.c).
(Note also [set_iterate.makefile](https://github.com/CS50Dartmouth21FS1/examples/blob/main/set_iterate.makefile).)

In this example, setA and setB each contain a set of school names, and a number for each school (say, perhaps, the number of people you know at each school).
When merging two sets you want the data in the resulting set to represent the sum of the values in each set.

```c
  set_insert(setA, "Dartmouth", intsave(20));
...
  printf("\nMerge of setA into result: \n");
  set_merge(result, setA);
  set_print(result, stdout, itemprint);
  putchar('\n');

  printf("\nMerge of setB into result: \n");
  set_merge(result, setB);
  set_print(result, stdout, itemprint);
  putchar('\n');
...

/* Merge the second set into the first set;
 * the second set is unchanged.
 */
static void 
set_merge(set_t* setA, set_t* setB)
{
  set_iterate(setB, setA, set_merge_helper);
}

/* Consider one item for insertion into the other set.
 * If the other set does not contain the item, insert it;
 * otherwise, update the other set's item with sum of item values.
 */
static void 
set_merge_helper(void* arg, const char* key, void* item)
{
  set_t* setA = arg;
  int* itemB = item;
  
  // find the same key in setA
  int* itemA = set_find(setA, key);
  if (itemA == NULL) {
    // not found: insert it
    set_insert(setA, key, intsave(*itemB));
    printf("\t%s added\n", key);
  } else {
    // add to the existing value
    *itemA += *itemB;
    printf("\t%s exists\n", key);
  }
}

static int* 
intsave(int item)
{
  int* saved = mem_assert(malloc(sizeof(int)), "intsave");
  *saved = item;
  return saved;
}
```

The overall structure of the code is identical to the prior example; the difference here is that we need to look up the key in the destination set first and then perhaps update its data - because we get a *pointer* to the data, we can easily reach in and update its value!

**[:arrow_forward: Video demo of the above](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=90cc69c1-4504-459f-9293-ad1601831e0b)**

