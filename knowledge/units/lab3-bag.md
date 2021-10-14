This unit is a quick video walk-through of **bag module** in the [Lab 3](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/labs/lab3) starter kit, and its Makefile.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c55eb0b8-0067-4118-a5be-ad0d0107a0b7)**

The module implements an opaque type and six functions:

```c
typedef struct bag bag_t;
bag_t* bag_new(void);
void bag_insert(bag_t* bag, void* item);
void* bag_extract(bag_t* bag);
void bag_print(bag_t* bag, FILE* fp, void (*itemprint)(FILE* fp, void* item));
void bag_iterate(bag_t* bag, void* arg, void (*itemfunc)(void* arg, void* item) );
void bag_delete(bag_t* bag, void (*itemdelete)(void* item) );
```

See `bag.h` for a full description of the behavior and interface to each function.
