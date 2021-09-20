In the next series of Labs we will design, implement and test a command-line web-search tool – called the Tiny Search Engine (TSE) because it can be written in under **2000** lines of student-written C code (about 500 lines in each of four Labs 3-6).
Here we begin to discuss the concepts behind web search and the top-level design of TSE and its decomposition into three major components: *crawler*, *indexer*, and *querier*.

First, we discuss some of the foundational issues associated with searching the web, and an architectural design of a more comprehensive search engine than TSE.

-   How does search engine like Google search the web?
-   A general search-engine architecture.
-   URLs, webpages, HTML, HTTP, and keywords.
-   'Talking' to the CS department webserver using HTTP.

## Searching the Web

How do you get information from the Web?
Searching the web is something we do every day with ease, but it's technically challenging to implement because of the scale of the web and because pages change at dramatically different rates.
According to [one estimate](http://www.worldwidewebsize.com) the indexed web contains at least **5.27 billion pages** as of 31 March 2021.
Even that number is likely an underestimate, because many organizations have lots of internal webpages that aren't indexed by the public search engines.

To get information about hiking in New Hampshire, I can use a search engine (such as Google) as an information retrieval system; it returns a list of links (URLs) to sites that have the keywords I specified embedded in them.
Conveniently, the search engine orders (ranks) the links so the most-relevant pages are near the top of the list.

![Google search for *hiking "new hampshire"*](media/search/hikenh.png)

------------------------------------------------------------------------

Google responded to my query in 0.52 seconds with 8,960,000 matches found!
How is that possible?
How does Google search billions of web pages in half of a second?
Surely, Google does not actually search those pages for each user's request.
Instead, it looks into a vast 'index' of the web, built earlier.
Given a new search term, it checks the index for pages known to have the word "hiking", and again for those with the phrase "new hampshire", and then intersects the two results to come up with a list.

How does Google rank the pages in the resulting list?
The solution is actually Google's 'secret sauce', the "page-rank algorithm" developed by Brin and Page when they were grad students.
(Although the original algorithm was [published](https://dl.acm.org/citation.cfm?id=297827), Google continues to refine and tweak it and keeps the details secret.)

When and how does Google build that index?
And how does it find all the pages on the web?
Google's servers are constantly "crawling" the web: given one link (URL), download that page, find all the links in that page, and then go examine those pages – recursively.
As new (or updated) pages are found, they "index" each page by extracting the list of words used on that page, and then building a data structure that maps from *words* to *URLs*.

Later, a search query is broken into words, and each word is sought in the index, returning a set of URLs where that word appears.
For a multi-word query, they intersect the sets to find a set where all words appear.
Then they apply the page-rank algorithm to the set to produce a ranked list.

In April 2014, Google's website said its index filled over 100 million gigabytes!
**Check out this [nice video](https://www.google.com/insidesearch/howsearchworks/crawling-indexing.html) from Google explaining how search engine works.**


### Search engine architecture [Arvind, 2001]

Search engines like Google are complex, sophisticated, highly distributed systems.
Below we reproduce the general search engine architecture discussed in *[Searching the Web](media/searchingtheweb.pdf).*

![General search engine architecture [Arvind, 2001]](media/search/searchenginearchitecture.png "General search engine architecture [Arvind, 2001]")

------------------------------------------------------------------------

The main components include parallel crawlers, crawler control (when and where to crawl), page repository, indexer, analysis, collection of data structures (index tables, structure, utility), and a query engine and ranking module.
Such a general architecture would take a significant amount of time to code.
In our TSE, we will implement a stripped down version of the main components.


### URLs, HTML, and keywords

Some terminology:

 * **URL**, short for *Uniform Resource Locator*, is used to specify addresses of webpages and other resources on the web.
An example is `http://www.dartmouth.edu/index.html`, which refers to the `HTTP` network protocol, the  `www.dartmouth.edu` server, and the `index.html` file on that server.
 * **HTML**. Most web pages are written in HyperText Markup Language (HTML).
For a quick tutorial on HTML see this *[Introduction to HTML](http://www.w3schools.com/html/html_intro.asp)*.
An HTML file is a text file with an `htm` or `html` file extension.
HTML pages can be created by tools or simply in an editor like emacs.
 * **tags**. HTML uses "tags" to mark-up the text; for example `<b>this text would be bold</b>`.
Most tags are enclosed in angle brackets, like `<b>`, and most come in matching pairs marking the beginning and ending of a region of text to which the tag applies; note the `<b>` and `</b>` pair.

We are interested collecting URLs from HTML files.
The HTML tag that forms a link and references a URL is called an 'anchor', or 'a' for short.
The tag `<a>` takes parameters, most importantly the `href` parameter:

```html
<a href="http://www.dartmouth.edu/index.html">Dartmouth home page</a>
```

For the purpose of indexing the page, we need to find the 'words' in the page.
In most web pages, most of the content is outside the tags because the tags are there to format the content.
**For TinySearchEngine, we define keywords as being outside of tags.**

So when TinySearchEngine downloads a webpage of HTML source it needs to parse the page to extract URLs (so it can crawl those URLs) and identify the words for which users might be interested in running queries.

Parsing HTML can be challenging, especially because so many pages on the web don't follow the HTML standard cleanly.
We will provide you with a C function to parse the HTML.

For more information about HTML check out the old [HTML 4 specification](http://www.w3.org/TR/REC-html40/about.html) or the new [HTML 5 specification](http://www.w3.org/TR/html5/).

### HTTP Demonstration

The [HyperText Transfer Protocol (HTTP)](http://www.w3.org/Protocols/) is used between your client browser and the server to transfer HTML files.
HTTP itself is a simple, stateless, request/response protocol that gets its reliable underlying transport from TCP (Transmission Control Protocol) on top of IP (Internet Protocol).
(Take CS60 to learn more about networking!)

The basic HTTP protocol is that the client sends a request called a `GET` and the server responds with a response.
Web servers 'listen' for requests on the well-known TCP port 80.

We have to first connect to the server and open up a reliable stream to host `www.cs.dartmouth.edu` at port 80.
We could write some fancy code to do this, but (in typical Unix style) there is already a command we can use: `telnet`, a program that simply makes a TCP/IP connection to a given host and port, and connects our terminal's keyboard and screen to that connection so we can interactively write to the remote server listening at that port.

We can thus type an HTTP `GET` request by hand:

```bash
$ telnet www.cs.dartmouth.edu 80
Trying 129.170.226.126...
Connected to www.cs.dartmouth.edu.
Escape character is '^]'.
GET /~cs50/index.html HTTP/1.1           <== I typed this
HOST: www.cs.dartmouth.edu               <== and this
                                         <== and a blank line
HTTP/1.1 301 Moved Permanently           <== here the server response begins
Date: Thu, 22 Apr 2021 23:22:38 GMT
Server: Apache/2.4.6 (Red Hat Enterprise Linux) OpenSSL/1.0.2k-fips mod_fcgid/2.3.9 Phusion_Passenger/6.0.8 PHP/5.4.16
Location: https://www.cs.dartmouth.edu/~cs50/index.html
Content-Length: 253
Content-Type: text/html; charset=iso-8859-1

<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html><head>
<title>301 Moved Permanently</title>
</head><body>
<h1>Moved Permanently</h1>
<p>The document has moved <a href="https://www.cs.dartmouth.edu/~cs50/index.html">here</a>.</p>
</body></html>
Connection closed by foreign host.
```

Try it yourself!
Make sure you hit carriage return twice after typing in
`HOST: www.cs.dartmouth.edu`
since the HTTP protocol wants an empty line to indicate that you're finished.

In this case, however, the server did not respond with the contents of the page because the CS web server now requires all visitors to use `https`, that is, the secure version of `http`.
We can't manually connect to an `https` server because everything has to be encrypted.

Fortunately, we run an old, unencrypted webserver just as a CS50 playground.
Let's connect to that server (`cs50tse.cs.dartmouth.edu`) instead:

```html
$ telnet cs50tse.cs.dartmouth.edu 80
Trying 10.192.42.79...
Connected to internal-load-balancer-private-01-649745155.us-east-1.elb.amazonaws.com.
Escape character is '^]'.
GET /tse/letters/index.html HTTP/1.1
HOST: cs50tse.cs.dartmouth.edu

HTTP/1.1 200 OK
Date: Thu, 22 Apr 2021 23:25:37 GMT
Content-Type: text/html
Content-Length: 105
Connection: keep-alive
Server: nginx
Last-Modified: Sat, 01 Feb 2020 04:10:52 GMT
ETag: "5e34fa4c-69"
Accept-Ranges: bytes

<html>
<title>home</title>
This is the home page for a CS50 TSE playground.
<a href=A.html>A</a>
</html>
^D
$
```

> You can see that this web server is actually a 'virtual' server running on Amazon AWS (cloud) service.

In the example we request a specific page on that server, `/tse/letters/index.html`.
The web server responds with a few lines of HTTP syntax; the first line reports status code `200` with human-readable equivalent `OK`.
The next few lines provide the date, the server version, the last-modified date for the file returned, and some other stuff describing the content as HTML.
After a blank line, the contents of the file we requested (a simple page we created for testing CS50 TSE).

This small web page includes a link to a file `A.html`.
Most web pages will include many links, and sometimes will include the same link twice.
Furthermore, when exploring a set of web pages, those web pages often link to each other (for example, all the CS50 pages link to the CS50 'home', and that page links directly or indirectly to all of those pages).

The web is a "directed graph", not a tree.
Any effort to 'crawl' the web, therefore, must use something like breadth-first or depth-first search to explore the graph, avoiding the effort (and infinite loops!) that occur when encountering a link already found earlier in the crawl.
