Web services
====

*old stuff that has not been updated*

Another way to think of a GET request is as a method call over the internet (this is the basic idea behind [REST](http://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm) where method parameters are URL parameters.  The return value is simply the (usually XML) data spit back by the server.  For example, here is how to access Yahoo's search engine via their REST interface to find information on the movie Serenity: [stale URL]

```java
http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=YahooDemo&query=serenity+movie
```

One could view this as an implementation of this method call:

```java
WebSearchService.webSearch("YahooDemo", "serenity+movie");
```

See [Creating a REST Request for Yahoo! Search Web Services](https://developer.yahoo.com/boss/search/) for more information.  Again, though, REST is just a way to think about access dynamic data from the web.

A POST request is almost the opposite of a GET request: it is meant to send data to the server from HTML forms (i.e., pages with `<form>` tags and submit buttons).  POST pages normally process the data and then redirect the browser to another page that says anything from "thanks" to "here is your requested data" (like when doing airline reservations).  You can think of GET services as display pages and POST services as processing pages.
