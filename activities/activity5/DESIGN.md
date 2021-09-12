# Weather map - draft Design spec

**This document is a sketch, not a complete spec.**

* original by Charles Palmer - 2021-02-08
* updated by David Kotz - 2021-04-28

## User interface

We were told "You may assume your program’s requests arrive on stdin, one per line, and your response to each request is printed to stdout."

## Inputs and outputs

_Input_: Input arrives on stdin.
The Requirements spec was not clear about the format, except to say "A request is a pair of floating-point numbers latitude, longitude, representing a point on the Earth."
We assume the pair is formatted as two floats on one line, separated by white space.

_Output_: The response is printed to stdout; one of the following:

- an XML-formatted weather report for the closest NWS weather station, 
- the string “out of bounds” if there is no NWS weather station within 100 miles of the request location, or
- a string "input error" if the request was badly formatted

## Functional decomposition into modules

The following modules are anticipated:

1. `main`, which initializes other modules and loops over input
2. `nws`, which knows how to connect to NWS and parse its xml
3. `cache`, which caches results from NWS

## Pseudo code for logic/algorithmic flow

**main:**

	initialize nws module
	initialize cache module
	inputLoop
		read one line from stdin
		extract latitude,longitude from line
		IF incorrect format
		THEN print "input error" and continue loop
		ELSE
			xml = cache_fetch(latitude, longitude)
			IF xml indicates out of bounds
			THEN print "out of bounds"
			ELSE print xml

**nws:**

	nws_init: initialize this module
	
	nws_list: returns a list of all weather stations and their locations
		connect to NWS server
		download list of station code names
		parse the response
		build and return an array of station codes
	
	nws_fetch: fetches xml for a given station, from NWS server
		connect to NWS server
		request xml for given station code
		download and return the xml
	
	nws_end: de-initialize this module
	
**cache:**

	cache_init: initialize this module
		create a quadtree structure
		foreach station in nws_list()
			xml = nws_fetch(station)
			add that station to the quadtree

	cache_fetch: returns xml for station closest to given location x,y:
		using the quadtree, find all the quadrants that include points within the range of longitudes [x-100miles:x+100miles] and the range of latitudes [y-100miles:y+100miles]

		IF no such quadrants found
		THEN return indication "out of bounds"

		compute the distance between (x,y) and each station in each of those quadrants, recording which station is closest

		IF the closest station is more than 100 miles from (x,y),
		THEN return indication "out of bounds"

		examine the xml for that closest station
		IF the xml metadata indicates NWS would likely have posted a newer observation by now,
		THEN nws_fetch(station) and replace our xml with fresh data
		
		return the xml for this station.
		
	cache_end: de-initialize this module
	

## Major data structures

The major data structure is a cache of xml fetched from NWS, indicated by (latitude, longitude) location.
The index is organized as a Quadtree [[Wikipedia](https://en.wikipedia.org/wiki/Quadtree)] [[CS10](https://www.cs.dartmouth.edu/~cs10/PS-2.html)], to make it easy to search for a point (x,y).

> The data structure is not a perfect fit: our goal is not simply to look up a point, but to find all stations whose location is within a range (100 miles) of a given point.
> See above for a rough description of one approach.

> Other data structures would be worth exploring.

## Performance

The Requirements specify a need for high *throughput* (one million requests per minute) and low *latency* (under 100ms per request).
Achieving both may be challenging.

Some observations:

* The service does not change any data as a result of user requests.
* All user requests are indepdendent of one another.
* The NWS data does not change quickly.

Thus, for high throughput it is possible to replicate the service:

* Every replica would cache data from NWS, and thus be effectively equivalent.
* Any user's request can be sent to any replica.
* Cloud services often have load-balancing front-ends that receive inbound requests and route them to a random replica – or to the least-loaded replica, etc.

And, for low latency,

* the *lazy* cache-update strategy may not suffice, because an `nws_fetch` may take a long time.
* the *periodic* cache-update strategy is therefore better, but can run as a "background thread", i.e., another *thread of control* within the server, updating xml within the cache while the "foreground thread" continues to serve new user requests.

 
## Testing plan

### Unit testing

**main:**

The `main` module will be unit-tested by providing 'stub' versions of the `nws` and `cache` modules that provide known behavior, not requiring network access, and with failure modes introduced at random (or by script), e.g., to simulate out-of-memory conditions, failed network connections, and invalid or extreme inputs.

**nws:**

The `nws` module will be unit-tested by writing a driver that invokes a crafted series of calls to module functions, probing all possible erroneous function parameters and pushing on edge cases.

Another unit test will push the module hard, to test its performance under heavy load.

The test code will simulate network failures, and slow responses from NWS servers.

**cache:**

The `cache` module will be unit-tested by writing a driver that invokes a crafted series of calls to module functions, probing all possible erroneous function parameters and pushing on edge cases.

Another unit test will push the module hard, to test its performance under heavy load.

The test code will simulate out-of-memory conditions.

### Integration testing

The complete server will be tested as a system, by driving it with a series of requests on stdin.
Two sets of inputs will be crafted, at least: one set of erroneous inputs and one set of valid inputs.
The valid inputs shall push on common cases as well as 'edge' cases.

#### Should Fail

> tbd, a list of test cases that should fail

#### Should Pass

> tbd, a list of test cases that should pass

