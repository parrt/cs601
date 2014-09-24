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

# REST

A criticism of SOAP/WSDL is the low-level procedural focus, which is very different from the model that has been successful for the web:

* A URL/URI refers to a unique resource which can be retrieved via HTTP.
* Focus is on retrieving data referenced with a particular name, as opposed to executing a sequence of operations.
* Rather than specifying how a client should interact with a service, we specify a reference to a data object in the form of a URI.
* Web as a shared information space, rather than as a medium for transporting messages between hosts. Encryption must be done at the network level if security is a concern.

**cons**: Challenging to encode data structures in a URI

Why use web? port 80 is open.

Amazon, Google Maps, Twitter, Yahoo!, Technorati, ... Now people are starting to require signed requests however. a serious pain, but understandable.

REST stands for Representational State Transfer

* Idea: Applications work with a representation of a resource (i.e. an XML representation of a song)
* These representations are shared, or transferred between components.
* These representations allow client applications to manage their state.
* Data-centric: all services and resources can be referenced with URIs.
Servers respond to a request by providing a representation of an object.
* a request:

http://api.twitter.com/1/followers/ids.json?screen_name=the_antlr_guy
XML or JSON is returned by the server. The client must determine how to validate and parse this, hopefully with the help of a schema.

REST is really more of an architectural model than a protocol. A recipe for building web-scale applications. In practice, it refers to:

* encoding requests within an URI
* using HTTP to deliver them
* returning results via XML/JSON

https://dev.twitter.com/rest/tools/console
https://code.google.com/p/python-twitter/

*stuff to do later*

JAX-RS Java API for RESTful Web Services is a Java programming language API that provides support in creating web services according to the Representational State Transfer (REST) architectural pattern. From version 1.1 on, JAX-RS is an official part of Java EE 6.

https://jersey.java.net/download.html
mv /Volumes/SSD2/Users/parrt/tmp/jaxrs-ri/lib /usr/local/lib/jersey-jaxrs
mv /Volumes/SSD2/Users/parrt/tmp/jaxrs-ri/api/javax.ws.rs-api-2.0.jar /usr/local/lib/jersey-jaxrs

jetty and xml/json and jersey

http://jlunaquiroga.blogspot.com/2014/01/restful-web-services-with-jetty-and.html
doc: https://jersey.java.net/documentation/latest/index.html

ugh: all examples seem to be 1.x not 2.0. fixed with this info:

https://java.net/projects/jersey/lists/users/archive/2013-06/message/91
http://stackoverflow.com/questions/18086218/java-lang-classnotfoundexception-com-sun-jersey-spi-container-servlet-servletco
