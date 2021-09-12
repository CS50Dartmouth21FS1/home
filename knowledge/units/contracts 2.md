A short note about an important form of commenting: a paragraph comment prior to every function declaration.
In CS50, we place detailed comments above each public function's declaration in the header `.h` file, explaining the function's interface and behavior to *users* of the module – that is, to other programmers who will be invoking these functions.
It is helpful to think of this paragraph comment as defining a ***contract*** between the caller (user of the module) and callee (the function within the module), most notably, about who is responsibile for allocating and freeing any chunks of memory involved.

You'll see these kinds of comments in our starter kits and in most of our examples; we expect you to write equally good comments in your code.

We recommend you copy-paste from the [contract.c](https://github.com/cs50dartmouth21FS1/examples/blob/main/contract.c) example, as a template for your code.

Consider an example, in `bag.h` that follows a slightly different format but conveys the same kind of information:

```c
/**************** bag_extract ****************/
/* Return any data item from the bag.
 *
 * Caller provides:
 *   valid bag pointer.
 * We return:
 *   return pointer to an item, or NULL if bag is NULL or empty.
 * We guarantee:
 *   the item is no longer in the bag.
 * Caller is responsible for:
 *   free-ing the item if it was originally allocated with malloc.
 * Note:
 *   the order in which items are removed from the bag is undefined.
 */
void* bag_extract(bag_t* bag);
```

whereas in `bag.c`, we do not repeat the information (lest the two copies become different and thus contradictory; we refer the reader to the header file):

```c
/**************** bag_extract() ****************/
/* see bag.h for description */
void*
bag_extract(bag_t* bag)
{
...
}
```

In some cases, the function-header comment within a `.c` file may need to include other implementation-specific notes, that is, notes relevant to readers and maintainers of the module's implementation, but not relevant to users of the module.
