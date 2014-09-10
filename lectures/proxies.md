Proxy Servers
=====

See also [Wikipedia entry on proxies](http://en.wikipedia.org/wiki/Proxy_server).

A proxy server is a tunneling server that:

* Responds to GET protocol commands that name the *complete URI* of the target resource (not just the local resource name `/foo` like a regular non-proxy HTTP request):
``GET http://xyz.com/foo HTTP/1.0``
* Responds to POST protocol commands, which passes the request body to the target resource:
``POST http://xyz.com/foo HTTP/1.1``

From this command, the proxy strips the hostname from the URI and passes the command onto the remote server as command:

```
GET /foo HTTP/1.0
```

for example.

To figure out how proxies work without screwing up your browser proxy settings, you can use curl:

```bash
curl --proxy localhost:8080 www.antlr.org
```

Note that in regular HTTP servers, the http://xyz.com/ part is in the Host: header.

![proxy](figures/h2g2bob.png)

Parameters for GET are in URI. For POST, they come as data elements after header + blank line:

```
...
Content-Type: application/x-www-form-urlencoded
Content-Length: 11

q=foo&x=bar
```

Useful for:

* hiding machines; proxy does all of the outside requests
* speed: proxies can cache commonly requested resources, increases bandwidth
* track and/or block user access
* sniff/scan data going out or coming in
* circumvent government web restrictions; I had to ssh to my box from Guangzhou to access a website once. All you need is a machine outside of the firewall. Set it up as a proxy to access the outside world and then have your browser pointed it.
* Wikipedia: "*To allow a web site to make web requests to externally hosted resources (e.g. images, music files, etc.) when cross-domain restrictions prohibit the web site from linking directly to the outside domains.*"   For example, JavaScript ajax programming wants to access twitter APIs, but security restrictions typically prevent this. Most JavaScript code can only access the server from which it comes.

## Headers

The proxy must collect all of the headers obtained from your browser and pass those along in its request to the remote server. Similarly, the headers received by the proxy from the remote server should be sent back to the browser as part of the response.

Here is what I get back from antlr.org

```bash
$ telnet www.antlr.org 80
Trying 138.202.170.10...
Connected to www.antlr.org.
Escape character is '^]'.
GET / HTTP/1.1
Host: www.antlr.org

HTTP/1.1 200 OK
Server: GitHub.com
Content-Type: text/html; charset=utf-8
Last-Modified: Wed, 16 Jul 2014 21:11:22 GMT
Expires: Wed, 03 Sep 2014 19:31:35 GMT
Cache-Control: max-age=600
Content-Length: 9792
...data...
```

POSTs look like:

```
POST http://host/file HTTP/1.0
content-type: application/x-www-form-urlencoded
cache-control: max-age=0
accept-language: en-US,en;q=0.8
host: localhost:8081
accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
content-length: 23
origin: null
accept-encoding: gzip,deflate

user=parrt&password=abc
```

Where `user=parrt&password=abc` is the POST data. Note the blank line after the headers and before the POST data.

Make sure you are reading *bytes* not *lines* or anything else. Do not interpret the data at all.

## Algorithm

To process a single request, given a `Socket` the basic algorithm goes like this:

1. get first line from browser
1. ignore and close the connection if there is no command
1. split into the command, the URI, and the HTTP version;
e.g. for this apart: "GET http://xyz.com/foo HTTP/1.0"
1. read in headers until you see a blank line; force the header names to lowercase and put the name-value pairs into a map
1. strip out user-agent, referer, proxy-connection headers
1. strip out connection header if its value is keep-alive
1. parse the URI to strip out the host and get the "file" name like /foo
1. open a socket at port 80 at the remote host
1. send it the same HTTP command that you got from the browser except make the version 1.0 not 1.1 so we don't have to worry about chunking and use the file name not the entire URI
1. then send the upstream host all of the headers we got from the browser minus the ones we deleted
1. if POST, get the `content-length` header from the browser and copy the data following the browser headers to the socket to the  upstream host
1. read the response line from the remote host like `HTTP/1.0 200 OK`
1. send it back to the browser
1. strip out connection header from the remote host response headers if its value is keep-alive
1. send  upstream host response headers back to the browser
1. read  upstream data and pass it back to the browser
1. flush and close the `Socket`

Some servers and browsers use different cases, so normalize your headers to all be lowercase. I tried this in my solution and it seems to work.

This algorithm does not deal with keep-alive connections. It forces browsers and servers to do one socket connection per request. Despite buffering input and output streams, browsing with my proxy is significantly less performant than without the proxy.

This mechanism handles all of the redirects and caching stuff with no problem. For example, in response to request

```
GET http://pagead2.googlesyndication.com/pagead/show_ads.js HTTP/1.1
```

The remote server might return

```
HTTP/1.0 304 Not Modified
```

which we can send directly back to the browser.

## Reverse proxy

A reverse proxy often acts like a load balancer and allows clients from outside of the company to access multiple resources from outside the firewall. A request to the reverse proxy triggers an internal request to the company's servers; that data is returned to the client. These kind of proxies can also do filtering and so on.

![](figures/revproxy.png)

A proxy server connecting the Internet to an internal network.

Reverse proxies can also have SSL acceleration hardware that removes the burden from the actual Web servers behind them.

## Network address translation vs Proxies

NAT is the stuff that your Internet cable modem router does to convert an outside public address to an internal private address like 10.0.0.1 or 192.168.0.5. All internal addresses flip to a single external address for outgoing traffic. That is done at the network layer whereas proxies are done at the application layer. Literally an application on the server is deciding to forward a request to another server.
