# Design clinic - solution notes

There are *many possible approaches* to the design of the service described in the Requirements spec.
Ours is just one approach.

### Draft specs:

We did not develop complete specs, but share here a sketch of each:

* [Design spec](DESIGN.md)
* [Implementation spec](IMPLEMENTATION.md)

### Some things to consider:

* A good **data structure** is important, because you need to quickly find the closest weather station to a given location; consider a Quadtree data structure [[Wikipedia](https://en.wikipedia.org/wiki/Quadtree)] [[CS10](https://www.cs.dartmouth.edu/~cs10/PS-2.html)].
* **Caching** is important, because you need quick access to weather data and cannot afford to download xml from NWS every time you need it (and the NWS may not be too happy if you're hitting their site millions of times per second!).
* **When to update the cache:**
  You could update the cache *periodically,* to ensure your cache always has fresh data for every station, or *lazily,* only pulling new data for a given station if the cached data for that station is outdated (i.e., the NWS server likely has newer data available).
  The former will ensure you can provide fresh data quickly, but the latter will reduce the load on NWS and on your network connection.
* NWS data changes slowly, relative to your users' access to data.
  Indeed, each xml file encodes information about when the data was last updated, and approximately when (and how often) it is updated.
  For example, station [KLEB](https://w1.weather.gov/xml/current_obs/KLEB.xml) appears to be updated hourly, 15 minutes after the hour:

        <suggested_pickup>15 minutes after the hour</suggested_pickup>
        <suggested_pickup_period>60</suggested_pickup_period>
		...
        <observation_time>Last Updated on Apr 28 2021, 8:53 am EDT</observation_time>
        <observation_time_rfc822>Wed, 28 Apr 2021 08:53:00 -0400</observation_time_rfc822>

  (You need to download the raw xml from that site, to see these details.)
* Your implementation should measure its performance, e.g., by recording the time of day, processing one million requests, then recording the time of day again; from those numbers you can compute the average response time.
  An operational service should log performance data so operators can stay aware of any unusual service loads or performance bottlenecks, and to provide raw data for future performance optimizations.
