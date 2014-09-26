Cookies
====

See also [a Tutorial on cookies in servlets](http://pdf.coreservlets.com/Cookies.pdf).

HTTP is stateless and anonymous with a very simple request response model.  In order to build websites like Amazon or any other website that lets you login, we need a mechanism for identifying users to come back to the same server. By identifying, we mean recognizing the same person, not actually knowing who they are.

HTTP is pretty simple but ~20 years ago, we came up with a good idea to support stateful communications between client/server. The idea is to piggyback key-value pairs called [*cookies*](http://docs.oracle.com/javaee/5/api/javax/servlet/http/Cookie.html) as regular old headers already allowed within the HTTP protocol. These key-value pairs therefore do not affect the data payload (stuff after the headers).

The cookie mechanism relies on a simple agreement between client and server. The sequence goes like this when a client visits a server for the first time:

1. Server sends back one or more cookies to the browser
2. Browser saves this information and then sends the same data back to the server for every future request associated with that domain

A cookie is a named piece of data (string) maintained by the browser that is sent to the server with every page request. The server can use that as a key to retrieve data associated with that user.

If you erase your cookies for that domain, the server will no longer recognize you. Naturally, it will try to send you cookies again. It is up to the browser to follow the agreement to keep sending cookies and to save data.

Can I, as a server, ask for another server's cookies (such as amazon.com's)? No! Security breach! If another server can get my server's cookies, the other server/person can log in as me on my server. Heck, my cookies might even store credit card numbers (bad idea). It is up to the browser to enforce this policy. Naturally, it could send every cookie or even every document on your computer via headers to a server!

A server can associate cookies with certain paths so that one set of cookies will be for URLs under `/news` and another for every other URL. Cookie names are, therefore, not unique even for the same hostname. A path of `/news` means the cookie is returned for any URI at `/news` or below.

A server can specify the lifetime of cookies in terms of seconds to live or that cookie should die when the browser closes. It can also tell the browser to delete a cookie immediately as part of the current request.

Cookies affect page caching. Can't cache a page if you need to send cookies back to the browser.

Another way to identify users is with so-called URL-rewriting that adds  unique identifier stuff to each URL but it's pretty cumbersome and makes for user-specific URLs that cannot be passed around easily. If you have cookies turned off, of course, some sites will switch to this mechanism. I think nowadays most people will simply say that you have to have cookies turned on.

# Sample HTTP traffic with cookies

My first request to `cnn.com` results in a cookie coming back from cnn (output is from my proxy server):

```
BROWSER: GET http://www.cnn.com/ HTTP/1.1
BROWSER HEADERS:
	host=www.cnn.com
	connection=Close
	accept=*/*
UPSTREAM HEADERS:
	date=Fri, 26 Sep 2014 18:33:38 GMT
	server=nginx
	set-cookie=CG=US:CA:San+Francisco; path=/
	last-modified=Fri, 26 Sep 2014 18:33:06 GMT
	expires=Fri, 26 Sep 2014 18:34:37 GMT
	vary=Accept-Encoding
	content-type=text/html
	connection=close
	cache-control=max-age=60, private
```

In the second HTTP request, we see that my browser is sending the cookie back to cnn:

```
BROWSER: GET http://www.cnn.com/robots.txt HTTP/1.1
BROWSER HEADERS:
	cookie=CG=US:CA:San+Francisco
	host=www.cnn.com
	connection=Close
	accept=*/*
UPSTREAM HEADERS:
	date=Fri, 26 Sep 2014 18:33:39 GMT
	server=nginx
	set-cookie=CG=US:CA:San+Francisco; path=/
	vary=Accept-Encoding
	x-ua-profile=desktop
	content-type=text/plain
	connection=close
	cache-control=max-age=60, private
```

 It comes from `antlr.org`:

```
BROWSER: GET http://www.antlr.org/ HTTP/1.1
cookie=__utma=38625011.418771076.1229726720.1380134178.1410384681.1073; __utmz=38625011.1410384681.1073.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)
```

# How ad companies track you

Notice that the browser will send cookies to the server even for images, not just webpages. Ad companies embed images and websites and therefore can send you a cookie that your browser dutifully stores. For example, here is the cookie ad traffic I got when I *opened an email* from `opentable.com`:

```
BROWSER: GET http://oascnx18015.247realmedia.com/RealMedia/ads/adstream_nx.ads/www.opentable.opt/email-reminder/m-4/4234234@x26 HTTP/1.1
BROWSER HEADERS:
	accept-language=en-us
	host=oascnx18015.247realmedia.com
...
UPSTREAM HEADERS:
	set-cookie=NSC_d18efmoy_qppm_iuuq=ffffffff09499e4a423660;path=/;httponly
	cache-control=no-cache,no-store,private
	pragma=no-cache
	...
```


There are a number of things to notice here:

1. The ad company, `realmedia.com`, tracks where the ad is using the URL: `www.opentable.opt/email-reminder/m-4/4234234@x26`. In other words, it knows that I opened the email from opentable. Yikes!
2. The headers turn off caching so that every time you refresh the page it gets notified.

Now, imagine that I go to a random website X that happens to have an ad from `realmedia.com`. My browser will send all cookies associated with `realmedia.com` to their server, effectively notifying them that I am looking at X. They will know about every page I visit that contains there ads.

Recently I was looking at hotels in San Diego and also purchasing some cat food on a different website. Then he went to Facebook and saw ads for the exact rooms and cat food I was looking for. This all works through the magic of cookies. There is a big ad clearinghouse where FB can ask if anybody is interested in serving ads to one of its users with a unique identifier. Hopefully they don't pass along your identity, but your browser still passes along your cookies for that ad server domain. The ad companies can then bid to send you an ad. Because your browser keep sending the same cookies to them regardless of the website, hotel and pet food sites can show you ads for what you were just looking at on a completely unrelated site. wow.

This technology is not all bad. Obviously, Google analytics requires a tiny little image the embedded in your webpages so that it can track things and give you statistics.

# Session cookies

So what does a server do with cookies? It typically needs to associate some data in memory with each incoming user request. We'll talk about these later, but here is what a typical response *session cookie* can look like (going to Amazon as a new user):

```
	set-cookie=session-id=124-2999-8223305; path=/; domain=.amazon.com; expires=Tue, 01-Jan-2036 08:00:01 GMT
```

and then the next request from the browser sends that cookie back:

```
BROWSER: GET http://www.amazon.com/robots.txt HTTP/1.1
BROWSER HEADERS:
	cookie=session-id=124-2999-8223305
	host=www.amazon.com
	connection=Close
	accept=*/*
```

# Accessing cookies in Java

## Set cookies

To send cookie back to the browser from a Java servlet:

1. set type of response to `text/html`
1. create `Cookie` object
1. `setMaxAge(interval-in-sec)` on cookie:
  * positive=num secs to live
  * negative = kill with browser
  * 0 = kill now
1. add cookie to `response`

```java
/** Set a cookie by name */
public static void setCookieValue(HttpServletResponse response,
								  String name,
								  String value)
{
	// Set-Cookie:user=parrt;Path=/;Expires=Thu, 25-Dec-2014 20:13:16 GMT
	Cookie c = new Cookie(name,value);
	c.setMaxAge( 3 * 30 * 24 * 60 * 60 ); // 3 months
	c.setPath( "/" );
	response.addCookie( c );
}
```

## Fetch cookies

The API provides only a method to get all cookies.  Have to search for
correct one:

```java
/** Find a cookie by name; return first found */
public static Cookie getCookie(HttpServletRequest request, String name) {
    Cookie[] allCookies;

    if ( name==null ) {
        throw new IllegalArgumentException("cookie name is null");
    }

    allCookies = request.getCookies();
    if (allCookies != null) {
        for (int i=0; i < allCookies.length; i++) {
            Cookie candidate = allCookies[i];
            if (name.equals(candidate.getName()) ) {
                return candidate;
            }
        }
    }
    return null;
}
```


```java
response.setContentType("text/html");
String user = request.getParameter("user");
setCookieValue(response, "user", ...);
```

See [CookieServlet.java](https://raw.githubusercontent.com/parrt/cs601/master/lectures/code/cookies/CookieServlet.java).

http://localhost:8080/cookies
http://localhost:8080/cookies?user=tombu

## Kill cookie

```java
private void killCookie(HttpServletResponse response, String name) {
    Cookie c = new Cookie(name,"false");
    c.setMaxAge( 0 ); // An age of 0 is defined to mean "delete cookie"
    c.setPath( "/" ); // for all subdirs
    response.addCookie( c );
}
```