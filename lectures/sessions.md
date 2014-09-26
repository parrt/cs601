Java server sessions
====

Cookies are the raw implementation mechanism by which servers can recognize returning users. They are pretty low level however and most servers use the notion of a *session* object that is automatically managed by the server, such as Jetty. In Java, this is an [`HttpSession`](http://docs.oracle.com/javaee/1.3/api/javax/servlet/http/HttpSession.html) object. ("*Provides a way to identify a user across more than one page request or visit to a Web site and to store information about that user. The server can maintain a session in many ways such as using cookies or rewriting URLs.*")

The server automatically create the session object and makes it available to the servlet via `request.getSession()`. The server can invalidate a session, which is a typical way to log somebody out.

Here is the key bit of code:

```java
// get session object for this session or create if new
HttpSession session = request.getSession();
```

Use `setAttribute(key,value)` to save object/value.

Use `getAttribute(key)` to get a value associated with a key.

Just use one session attribute and store all variables in there rather than multiple session variables.

Here are my typical session variables

```java
/** Session variable pointing to a user object */
public static final String SESSION_MEMBER = "user";

/** Indicates currently logged in (could be just visiting site) */
public static final String SESSION_LOGGEDIN = "loggedin";
```

```java
HttpSession session = request.getSession();
User u = (User)session.getAttribute("user");
if ( u==null ) {
	u = new User("parrt", 234123434);
	session.setAttribute("user", u);
}
```

Here is how you can "log in" a user:

```java
public void login(HttpSession session, User user) {
    session.setAttribute(SESSION_LOGGEDIN, "true");
    session.setAttribute(SESSION_MEMBER, user);
}
```

and logout:

```java
public void logout(HttpSession session) {
    session.removeAttribute(SESSION_LOGGEDIN);
    session.removeAttribute(SESSION_MEMBER);
    session.invalidate();
}
```

Here is an example @(sessions/Login.java, Login page) and simple @(sessions/PageCount.java, pagecount page).