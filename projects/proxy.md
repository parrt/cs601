Building a proxy server
=======

## Goal

Your goal in this project is to build a multi-threaded web proxy server usable by common web browsers to access remote hosts anonymously.  Your proxy need only respond to GET and POST requests.  

## Discussion

This project is all about the low level HTTP protocol commands and so you can only use the low level Java socket and I/O libraries. That means you cannot use any of the URL/HTTP support objects like URL and URLConnection.

My solution works very well with complicated websites like nytimes.com and some JavaScripty sites. I have checked it for POSTs as well. It is only 225 lines of Java code including comments, but the details took me a while to get right. Total development time was less than six hours (minus time to re-factoring cleaned it up). Depending on your development experience, you might take longer (or even less time because I have given you the algorithm and the key header manipulations in the proxy lecture notes.)  The details are not difficult but you have to get all of them correct to make this work.

To test your proxy server, you can set your browser preferences to use a proxy. That way you can see if all the images come through and that the page looks right. Eventually that gets annoying, so it makes more sense to automate the process. The key to a successful test is that ``wget`` yields identical directories with and without the use of the proxy:

```bash
$ export http_proxy=http://localhost:8080
$ wget -nv -P withproxy --proxy=on -r --level 1 http://www.cs.usfca.edu
2014-08-25 12:56:52 URL:http://www.cs.usfca.edu/ [43200/43200] -> "www.cs.usfca.edu/index.html" [1]
http://www.cs.usfca.edu/robots.txt:
2014-08-25 12:56:52 ERROR 404: Not Found.
2014-08-25 12:56:52 URL:http://www.cs.usfca.edu/_/rsrc/1407908848000/system/app/css/overlay.css [4099/4099] -> "www.cs.usfca.edu/_/rsrc/1407908848000/system/app/css/overlay.css" [1]
...
$ wget -nv -P noproxy --proxy=on -r --level 1 http://www.cs.usfca.edu
...
$ diff -r noproxy withproxy # should be the same
$
```

Note that our CS website does not change from request to request, but many other websites do. For example, github sends timing information and other special numbers for sessions and things. The diff It will not be exactly the same. Here are two lines from github, the first one without the proxy in the second through the proxy:

```html
<li>&copy; 2014 <span title="0.00480s from github-fe137-cp1-prd.iad.github.net">GitHub</span>, Inc.</li>
```

```html
<li>&copy; 2014 <span title="0.00510s from github-fe132-cp1-prd.iad.github.net">GitHub</span>, Inc.</li>
```
 Don't worry about these small differences. It's obvious that the proxy is working still.

## Your task

Please create two classes: `ProxyServer` and `ClientHandler`. The `ProxyServer` has the `main` program and does nothing but launch `ClientHandler` objects, one per socket connection. The algorithm is essentially just:

```
Open a server socket
loop forever:
    wait for a socket connection
	create a client handler, passing the Socket object
	create and launch a Java thread attached to that client handler
```

You can allow `IOException`s associated with function calls in `main` to flow out of the main program, killing the server.

It's a good idea to start out with a single threaded server so that is easier to debug. As you might have errors later, I use a flag to turn this on and off:

```java
public static final boolean SingleThreaded = false;
```

A single threaded server completes the job associated with each socket connection before accepting another.

Your `ClientHandler` should be runnable so that you can launch a thread on it. You must also create a constructor that allows the `main` to pass it a Socket:

```
public class ClientHandler implements Runnable {
	...
	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
```

It's a good idea to have a debug flag in `ClientHandler` that, when true, prints out all sorts of interesting information. Here is what mine prints out (the 63787 is the port number associated with the individual socket connection, not the server socket):

```
SOCKET 63787
BROWSER: GET http://www.cs.usfca.edu/index.html HTTP/1.1
BROWSER HEADERS: {connection=Close, host=www.cs.usfca.edu, accept=*/*}
SENT UPSTREAM: GET /index.html HTTP/1.0
UPSTREAM REPLY: HTTP/1.1 200 OK
UPSTREAM HEADERS:  {content-type=text/html; charset=UTF-8, connection=close, content-length=43200, accept-ranges=bytes, server=Apache/2.2.15 (CentOS), date=Wed, 27 Aug 2014 21:51:37 GMT}
READING UPSTREAM DATA
DONE WRITING BACK 43200 BYTES TO BROWSER
FLUSH BROWSER SOCKET 63787
CLOSE BROWSER SOCKET 63787
```

You don't have to follow this recommendation, but the list of methods in class `ClientHandler` that I have is:

```java
forwardRemoteDataToBrowser()
getHeaders()
makeUpstreamRequest()
openUpstreamSocket()
process()
readLine()
run()
writeHeaders()
```

## Requirements

* Have your proxy listen at port specified from the command line as the first argument, defaulting to port 8080 if none is specified.
* Follow the algorithm from the proxy lecture notes.
* Launch new thread to handle each new incoming connection otherwise your proxy will make browsing super slow.
* Strip ``User-Agent,Referer`` browser headers (to browse incognito) before sending onto the upstream server to browse and incognito mode:
```
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) ...
Referer: http://antlr.org/submit/challenge?type=grammar
```
Also strip out `proxy-connection` header.
* Make sure that you trap "host not found" exceptions; I found one case where the New York Times website sent my proxy a ".comp" not ".com"-based hostname.  In this case, the proxy must send back a "HTTP/1.0 400 Bad Request" response to the browser because the requested upstream server does not exist.
* You must send appropriate responses from the remote host back to the browser. For example, if the incoming server sees
```
GET http://antlr.org/bad HTTP/1.1
```
then antlr.org will return
```
HTTP/1.1 404 Not Found
```
and usually some payload data
* In response to a POST, many servers send a redirect back. Your proxy will get back the following response from the remote host, which you must pass along to the browser including all of the headers you get from the remote host. (They contain the forwarding address)
```
HTTP/1.1 302 Moved Temporarily
```
* Use buffered input and output connections to the browser and the remote hosts.  Warning: It's tricky figuring out when to flush these buffers.
* *You must not try to buffer the entire return payload in memory*. In other words, once you establish connection with the upstream server, pull the data over in blocks that you can send back to the client browser.
* *You must use the raw Java socket library to create your proxy server*. We are trying to build a proxy that must look at the HTTP protocol incoming on the sockets. We cannot do this if you're using a standard Web server that handles all of that stuff for us; such classes simply inform us that a ``GET`` or ``POST`` has occurred. we need something lower-level. You cannot use classes like ``URL`` and ``URLConnection``.

## Submission

You will follow the usual mechanism of using github. A continuous integration build server will run your program by cloning your repo, building it, and running some unit tests.  Your repo will be ``userid-proxy``.

You may discuss this project in its generality with anybody you want and may look at any code on the internet except for a classmate's code. You should physically code this project completely yourself but can use all the help you find other than cutting-n-pasting or looking at code from a classmate or other Human being. There is a lot of sample code out there for implementing Java proxies, but they all are these massive programs that use lots of helper objects. For example, my program doesn't use any helper classes except for the Runnable client handler.  I've looked at most of these solutions on the web and I will be very suspicious if I see a similar coding pattern. You must implement this yourself.

I will deduct 10% if your program is not executable exactly in the fashion mentioned in the project; that is, class name, methods, lack-of-package, and jar must be exactly right. For you PC folks, note that case is significant for class names and file names on unix! All projects must run properly under linux at amazon.
