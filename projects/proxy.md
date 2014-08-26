Building a proxy server
=======

## Goal

Your goal in this project is to build a multi-threaded web proxy server usable by common web browsers to access remote hosts anonymously.  Your proxy need only respond to GET and POST requests.  

## Discussion

This project is all about the low level HTTP protocol commands and so you can only use the low level Java socket and I/O libraries. That means you cannot use any of the URL/HTTP support objects like URL and URLConnection.

My solution works very well with complicated websites like nytimes.com and some JavaScripty sites. I have checked it for POSTs as well. It is only 200 lines of Java code including comments, but the details took me a while to get right. Total development time was less than six hours. Depending on your development experience, you might take longer (or even less time because I have given you the algorithm and the key header manipulations in the proxy lecture notes.)  The details are not difficult but you have to get all of them correct to make this work.

To test your proxy server, you can set your browser preferences to use a proxy. That way you can see if all the images come through and that the page looks right. Eventually that gets annoying, so it makes more sense to automate the process. The key to a successful test is that ``wget`` yields identical directories with and without the use of the proxy:

```
$ export http_proxy=http://localhost:8080
$ wget -nv -P withproxy --proxy=on -r --level 1 http://www.cs.usfca.edu
2014-08-25 12:56:52 URL:http://www.cs.usfca.edu/ [43200/43200] -> "www.cs.usfca.edu/index.html" [1]
http://www.cs.usfca.edu/robots.txt:
2014-08-25 12:56:52 ERROR 404: Not Found.
2014-08-25 12:56:52 URL:http://www.cs.usfca.edu/_/rsrc/1407908848000/system/app/css/overlay.css [4099/4099] -> "www.cs.usfca.edu/_/rsrc/1407908848000/system/app/css/overlay.css" [1]
...
$ wget -nv -P noproxy --proxy=on -r --level 1 http://www.cs.usfca.edu
$ diff -r noproxy withproxy # should be the same
$
```

``ProxyServer`` and ``ClientHandler``

## Requirements

* Have your proxy listen at 8080.
* Follow the algorithm from the proxy lecture notes.
* Launch new thread to handle each new incoming connection otherwise your proxy will make browsing super slow.
* You must deploy your proxy server on the Amazon Web services machine I provide for you.  The studentkey.pem key file that you need to log into your server is in /home/public/cs680 on the Stargate machine. This is the same file used by all students but only you know what your machine name is so it acts like a password. Do not give out your machine name to others because they can't log into your machine and copy your software.
* Strip User-Agent,Referer headers (to browse incognito):
```
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) ...
Referer: http://antlr.org/submit/challenge?type=grammar 
```
* Make sure that you trap "host not found" exceptions; I found one case where the New York Times website for sending me a ".comp" not ".com"-based hostname. In that case, send back a "HTTP/1.0 400 Bad Request" response to the browser.
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
HTTP/1.1 302 Moved Temporarily
* Buffer the input and output connections to the browser and the remote hosts.  Warning: It's tricky figuring out when to flush these buffers.
* You must not try to buffer the entire return payload in memory. In other words, once you establish connection with the remote server, pull the data over in blocks that you can send back to the client browser.
* You must use the raw Java socket library to create your proxy server. We are trying to build a proxy that must look at the HTTP protocol incoming on the sockets. We cannot do this if you're using a standard Web server that handles all of that stuff for us; such classes simply inform us that a ``GET`` or ``POST`` has occurred. we need something lower-level. You cannot use classes like URL and URLConnection.

## Submission

You will create a jar file called proxy.jar containing *.class files and place it in a directory called proxy/dist under your cs680 dir:

https://www/svn/userid/cs680/proxy/dist/proxy.jar
Pur your source Java code in wc/src:

https://www/svn/userid/cs680/proxy/src/...
To jar your stuff up, you will "cd" to the directory containing your source code (perhaps wc/src) and create the jar in the wc dir:

cd ~/cs680/proxy/src
jar cvf ~/cs680/proxy/dist/proxy.jar *.class
cd ~/cs680/proxy/dist
svn add proxy.jar
svn commit
All classes must be in the default package!

To learn more about submitting your project with svn, see Resources.

You must submit your source code for credit.

You must also leave a copy of your proxy.jar file in the root's home directory on your Amazon machine: /root. Here is how you will login:

ssh -i studentkey.pem root@ec2-xxxxx.compute-1.amazonaws.com
Using scp to copy your jar up there is fairly similar. 

Note that ssh/scp will not allow you to login using that key file if it is visible to other users read on UNIX, I set the permissions like this:

$ chmod 600 AWS/studentkey.pem
Grading
I will use your proxy at Amazon with my firefox browser to test your software. I will set my proxy to your machine name and port 8080. Because your software might crash, I need the ability to restart it. Please leave a copy of your proxy.jar in the root home directory on your Amazon machine.  Your project must start up exactly like this:

$ java -cp /root/proxy.jar:$CLASSPATH ProxyServer
You may discuss this project in its generality with anybody you want and may look at any code on the internet except for a classmate's code. You should physically code this project completely yourself but can use all the help you find other than cutting-n-pasting or looking at code from a classmate or other Human being. There is a lot of sample code out there for implementing Java proxies, but they all are these massive programs that use lots of helper objects. For example, my program doesn't use any helper classes except for the Runnable client handler.  I've looked at most of these solutions on the web and I will be very suspicious if I see a similar coding pattern. You must implement this yourself.

I will deduct 10% if your program is not executable exactly in the fashion mentioned in the project; that is, class name, methods, lack-of-package, and jar must be exactly right. For you PC folks, note that case is significant for class names and file names on unix! All projects must run properly under linux at amazon.
