**This document is a sketch, not a complete spec.**

* original by Charles Palmer - 2021-02-08
* updated by David Kotz - 2021-04-28

## Data structures

The major data structure is implemented by the `cache` module, as a quadtree.
Here is one straightforward approach, in which each tree node is either a leaf or a non-leaf, and every leaf contains exactly one station.

```c
// represent a specific station
typedef struct station {
	char code[5];              // station 4-letter code
	float latitude, longitude; // location of this station
	char* xml;                 // most recent weather data
	time_t updated;            // xml last-update time
} station_t;

// represent a tree (or subtree)
typedef struct quadtree {
	station_t* station;  // if non-NULL, this is a leaf node
	// for a non-leaf node, specify region and 1..4 subquads
	float minLat, maxLat, minLong, maxLong;
	struct quadtree* ne; // NE subquadrant
	struct quadtree* se; // SE subquadrant
	struct quadtree* nw; // NW subquadrant
	struct quadtree* sw; // SW subquadrant
} quad_t;

// represent the whole cache; for now, just a tree
typedef struct {
	quad_t *tree;
} cache_t;

// global variable (internal to the cache module)
static cache_t cache;

```


## Control flow for overall flow

	nws_init()
	cache_init()
	while ( not EOF on stdin )
		read one line from stdin
		IF failed in extracting latitude,longitude from line
		THEN print "input error"
		ELSE
			xml = cache_fetch(latitude, longitude)
			IF xml == NULL
			THEN print "out of bounds"
			ELSE print xml


## Control flow for each of the functions

Most are straightforward and not covered in this sketch.
`cache_fetch` is the core challenge.
This implementation is recursive, building on a local 'helper' function `findStation` that returns the `station_t*` indicating the closest station within a given quadtree.

Thus, the `cache_fetch()` function starts the recursion and, if a station is found, returns the xml:

	station_t* s = findStation(cache.tree, latitude, longitude)
	IF s not NULL
	THEN // s is the closest station!
		IF s->timestamp is not sufficiently recent
		THEN s->xml = nws_fetch(s->code)
		return s->xml
	ELSE return NULL

function `findStation(tree, lat,long)` then looks for that location in the given tree:

	IF tree->station not NULL
	THEN // leaf
		IF distance (tree->station, lat, long) <= 100miles
		THEN return tree->station
		ELSE return NULL

	ELSE // non-leaf
	closest = NULL
	for each non-NULL subquadrant q of tree
		IF any location in q is < 100miles from (lat, long)
		THEN
			s = findStation(q, lat, long)
			if closest is NULL
			OR distance(s, lat, long) < distance(closest, lat, long)
			THEN closest = s
	return closest

## Detailed function prototypes

```c
void nws_init(void);
char** nws_list(void); // returns array of string pointers, last one null
char* nws_fetch(const char* stationCode);
void nws_end();
```

```c
void cache_init(void);
char* cache_fetch(const float lat, long);
void cache_end(void);
// local helper functions
station_t* findStation(quad_t* tree, const float lat, long);
```


## Error handling and recovery

The implementation shall use defensive-programming techniques to ensure any error (out of memory, network failure, etc.) is caught and recovered as best possible.
In the worst case, the service crashes; a 'watchdog' script should re-launch the service. 

To provide a reliable service, replicate the service across multiple servers located in multiple locations (with independent power, cooling, and network connections), and direct inbound requests to one of the currently operating servers.
A failed service instance can be taken out of the list for serving new requests.

## Persistent storage (files, database, etc)

No persistent storage is needed – all the 'real' data lives at NWS, and our server only caches that data.

To survive power failures, server crashes, etc., we could write code to save the cache to a file (or database) and more code to re-load the cache from the database, but it is far easier to simply re-load it from NWS.


## Testing plan

> details tbd.

