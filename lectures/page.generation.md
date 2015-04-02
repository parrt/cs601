Dynamic Web Page Generation
====

# Introduction

Static web sites are a collection of HTML files.  Dynamic sites like Amazon.com, on the other hand, require that you generate pages on the fly.  This essentially means that your site becomes an application instead of a bunch of text files.  Your web server must now map a URL to a snippet of executable code that spits out HTML rather than mapping to a file.

In the Java world, there are 2 main choices for executing raw server-side-Java:

1. *Servlets*: HTTP protocol requests are routed to `doGet()` and `doPost()` methods in an `HttpServlet` subclass.  Your code has embedded HTML.
1. *JSP*: An HTML file with embedded, unrestricted Java code.  Implemented as the `doGet()` method of an auto-generated `Servlet`.

JSP files are enticing at first as they are easier to use than servlets, but they encourage all sorts of bad habits are hard to deal with for large sites.

The best approach is servlets + a template engine.  A template engine is usually an MVC (model-view-controller) based scheme where a template (the _view_) has "holes" it that you can stick values, computed by your business logic (the _model_).  The "holes" are typically restricted to just attribute references or simple computations/loops to avoid re-inventing JSP.  The web server + page code executed for a URL embody the _controller_.  There are a million of these engines such as [Velocity](http://velocity.apache.org/engine/devel/), FreeMarker, [StringTemplate](http://www.stringtemplate.org), Tea, ...

To understand why the servlet + template engine approach is superior, it helps to look at the path all developers seem to follow on their way to template engines.

# Evolution of template engines

How can you generate HTML from a servlet to send to a client's browser?  The obvious first choice is to produce a servlet that uses PrintStream.println() to construct valid HTML and send it to the output stream as in the following example.

```java
public void doGet(HttpServletRequest request,
                  HttpServletResponse response)
    throws ServletException, IOException
{
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<html><body>");
    String name = request.getParameter("name");
    out.println("Hello "+name);
    out.println("</body></html>");
    out.close();
}
```

There are a few problems with this approach.

## Problem 0: it's inside out!

The problem is that specifying HTML in Java code is tedious, error-prone, and cannot be written by a graphics designer.  Shouldn't we invert it so we embed the Java not the HTML?  Sounds good; enter JSP:

```java
<html>
<body>
Hello <%=request.getParameter("name")%>
</body>
</html>
```

Everyone was very excited by in 1999 when this came out as it was really fast to slam out some dynamic HTML pages.  Trouble arose, however, when people started building big sites with lots of dynamic pages:

* JSP files may start out as HTML files with simple references to data, as in this example, but they quickly degenerate into fully entangled code and HTML specifications like Servlets.

* JSP encourages bad object-oriented design. For example, an include file is a poor substitute for class inheritance. JSP pages may not be subclassed, which makes it hard for programmers to factor out common code and HTML. [Jason Hunter](http://www.servlets.com/soapbox/problems-jsp.html) summarizes a few other problems with JSP.

JSP has grown a lot since 1999, but it has not solved the real problem of separating the data model from it's presentation.  See below for a discussion of template engines.

Ok, so it's back to servlets then.

### Problem 1: Factoring

Another problem relates to factoring. If all pages are supposed to have the same look, you'll have to repeat all of the print statements such as

```java
    out.println("<html><body>");
```

in each servlet.  Naturally, a better way is to factor out this common code.  In the OO world that means creating a superclass that knows how to dump the header and footer like this:

```java
public class PageServlet extends HttpServlet {
    PrintWriter out;
    public Page() {out = response.getWriter();}
    public void header(HttpServletRequest request,
               HttpServletResponse response)
    throws ServletException, IOException
    {
        response.setContentType("text/html");
        out.println("<html><body>");
    }
    public void footer(HttpServletRequest request,
                       HttpServletResponse response)
        throws ServletException, IOException
    {
        out.println("</body></html>");
        out.close();
    }
}
```

then your servlet would be

```java
public class HelloServlet extends PageServlet {
    public void doGet(HttpServletRequest request,
              HttpServletResponse response)
        throws ServletException, IOException
    {
        header(request, response);
        String name = request.getParameter("name");
        out.println("Hello "+name);
        footer(request, response);
    }
}
```

## Problem 2: Objectionable OO Design

The second problem relates to design.  Is `Page` a kind of `Servlet`?  Not really.  A servlet is merely the glue that connects a web server to the notion of a page that generates output.  One could for example reuse the notion of a page to generate output for use in a non-server app that happened to use HTML to display information (e.g., with HotJava java-based browser component).

A better OO design would result in a servlet that creates an instance of a page (solving some threading issues) and invokes its display methods.

Further, *you should attempt to isolate your application from the quirks and details of the web server, doing as much as you can in code*.  You may switch web servers from TomCat to Resin to Jetty etc... and, in general, you just generally have more control in code than in the server config file.  For example, you should do your own page caching as you know better than the web server when data becomes stale.

Here is a generic `Page` object (decoupled from the details of Servlets except for the request/response objects):

```java
public class Page {
    HttpServletRequest request;
    HttpServletResponse response;
    PrintStream out = ...;
    public Page(HttpServletRequest request,
                HttpServletResponse response)
    {
        this.request = request;
        this.response = response;
        out = response.getWriter();
    }
    public void generate() {
        header();
        body();
        footer();
    }
    public void header() {...}
    public void body() {...}
    public void footer() {...}
}
```

A simple hello page might look like this:

```java
public class HelloPage extends Page {
    public HelloPage(HttpServletRequest request,
                     HttpServletResponse response)
    {
        super(request, response);
    }
    public void body() {
        String name = request.getParameter("name");
        out.println("Hello "+name);
    }
}
```

The servlet to invoke a Page object would look like:

```java
public class HelloPageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException
    {
        Page p = new HelloPage(request, response);
        p.generate();
    }
}
```

But, making a servlet for each page is a hassle and unnecessary.  It is better to create one servlet with a table that maps URL to `Page` object. (In the parlance of MVC, your controller is made up of a single `Servlet` and the `Page` subclass).

```java
public class DispatchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException
    {
        String uri = request.getRequestURI();
        Page p = lookupPage(uri);
        p.generate();
    }
    public Page lookupPage(String uri) {
        if ( uri.equals("/hello") ) {
            return new HelloPage(request, response);
        }
        if ( uri.equals("/article/index") ) {
            return new ArticleIndexPage(request, response);
        }
	...
    }
}
```

Naturally the if-then sequence in `lookupPage()` method should be coded as a `HashMap` that maps URI (URI is a more general URL) to a `Class` object:

```java
public HashMap<String,Class<? extends Page>> uriToPageMap = new HashMap<>();
...
uriToPageMap.put("/hello", HelloPage.class);
uriToPageMap.put("/article/index", ArticleIndexPage.class);
...
```

Then your `doGet()` method would use reflection to find the appropriate `Page` subclass constructor and create a new instance like this:

```java
public void doGet(HttpServletRequest request,
                  HttpServletResponse response)
    throws ServletException, IOException
{
    String uri = request.getRequestURI();

    // which Page subclass does uri map to?
    Class c = uriTopageMap.get(uri);

    // Create an instance using correct ctor
    Constructor ctor = c.getConstructor(HttpServletRequest.class,
										HttpServletResponse.class);
    Object[] ctorActualArgs = new Object[] {request,response};
    Page p = (Page)ctor.newInstance(ctorActualArgs);

    // finally generate output
    p.generate();
}
```

You need to set your web server to map all URLs (but images etc...) to your dispatcher so any `/x/y?args` lands you in your single servlet.

Now, you have totally isolated your project code from the notion of how pages are requested.  You could, for example, build a cmd-line tool that generated pages.

You can also make your `Page` object handle argument processing and so on.  Just add a `verify()` method:

```java
public class Page {
    ...
    public void verify() throws VerifyException {
        // handle default args like page number, etc...
        // verify that arguments make sense
        // implemented by subclass typically
	// VerifyException is a custom Exception subclass
    }
    public void handleDefaultArgs() {
        // handle default args like page number, etc...
        pageNum = request.getParameter("page");
    }
    public void generate() {
        out = response.getWriter();
        handleDefaultArgs(); 
        try {
            verify(); // check args before generation
            header();
            body();
            footer();
        }
        catch (VerifyException ve) {
            // redirect to error page
        }
    }
}
```

Your `HelloPage` could then be augmented as follows:

```java
public class HelloPage extends Page {
    ...
    public void verify() throws VerifyException {
        if ( request.getParameter("name")==null ) {
	        throw new VerifyException("Name argument required!");
		}
	}
    public void body() {
        String name = request.getParameter("name");
        out.println("Hello "+name);
    }
}
```

## Problem 3: Entangled Model-View

Eventually everyone reaches the conclusion that you must separate the code and the HTML template using a template mechanism of some kind in an attempt to make web application development easier, improve flexibility, reduce maintenance costs, and allow parallel code and HTML development.

The mantra of every experienced web application developer is the same: _thou shalt separate business logic from display_.  

One look at the following servlet should give you the idea (taken from an old Jetty example):

```java
public void completeSections() {
    if ("/index.html".equals(_path)) {                            
        Block _resourceHead = new Block ("div","class=menuRightHead");
        _resourceHead.add("Related sites:");
        _divRight.add(_resourceHead);
        _divRight.add("<div class=\"menuRightItem\"><A HREF=\"http://www.mortbay.com\">"+
            "<IMG SRC=\""+_context+"/images/mbLogoBar.gif\" WIDTH=120 "+
            "HEIGHT=75 ALT=\"Mort Bay\"></A></div>");       
        _divRight.add("<div class=\"menuRightItem\">"+
            "<A HREF=\"http://sourceforge.net/projects/jetty/\">");
        if (__realSite)
            _divRight.add("<IMG src=\"http://sourceforge.net/sflogo.php?group_id=7322\""+
            "width=\"88\" height=\"31\" border=\"0\" alt=\"SourceForge\">");
        else
            _divRight.add("<IMG SRC=\""+_context+"/images/sourceforge.gif\""+
            " WIDTH=88 HEIGHT=31 BORDER=\"0\" alt=\"SourceForge\"></A>");
        _divRight.add("</A></div>");
...
```

The key principle is to separate the specification of a page's business logic and data computations from the specification of how a page displays such information. With separate encapsulated specifications, template engines promote component reuse, pluggable, single-points-of-change for common components, and high overall system clarity.

* **dynamic**. You can change the templates on-the-fly w/o restarting the server. Further, conditionals are evaluated dynamically so that the look can change depending on arguments.
* **structured**. The components of your system such as a search box template are neatly organized into separate components. Each page is a hierarchy of nested templates. Analogous to good functional decomposition.
* **reusable components**. With a good structured design, you are encouraged to reuse components.
* **templates groups**. To have multiple looks for jGuru, we used a StringTemplateGroup that essentially provides a scoping mechanism. When you reference template searchbox, for example, you will get the search box template associated with your group/look. Organizing your site look into structured, reusable components makes inheritance possible.
* **strict model-view separation**. Allows programmer and graphics designer to work independently and in parallel. We verified this ability many times.


I have discussed the principle of separation with many experienced programmers and have examined many commonly-available template engines used with a variety of languages including Java, C, and Perl.  Without exception, programmers espouse separation of logic and display as an ideal principle.  In practice, however, programmers and engine producers are loath to enforce separation, fearing loss of power resulting in a crucial page that they cannot generate while satisfying the principle.  Instead, they _encourage_ rather than _enforce_ the principle, leaving themselves a gaping "backdoor" to avoid insufficient page generation power.

Unfortunately, under deadline pressure, programmers will use this backdoor routinely as an expedient if it is available to them, thus, entangling logic and display.  One programmer, who is responsible for his company's server data model, told me that he had 3 more days until a hard deadline, but it would take 10 days to coerce programmers around the world to change the affected multilingual page displays.  He had the choice of possibly getting fired now, but doing the right thing for future maintenance, or he could keep his job by pushing out the new HTML via his data model into the pages, leaving the entanglement mess to some vague future time or even to another programmer.

The opposite situation is more common where programmers embed business logic in their templates as an expedient to avoid having to update their data model.  Given a Turing-complete template programming language, programmers are tempted to add logic directly where they need it in the template instead of having the data model do the logic and passing in the boolean result, thereby, decoupling the view from the model.  For example, just about every template engine's documentation shows how to alter the display according to a user's privileges.  Rather than asking simply if the user is "special", the template encodes logic to compute whether or not the user is special.  If the definition of special changes, potentially every template in the system will have to be altered.  Worse, programmers will forget a template, introducing a bug that will pop up randomly in the future.  These expedients are common and quickly result in a fully entangled specification. 

A template should merely represent a view of a data set and be totally divorced from the underlying data computations whose results it will display.

Many template engines have surfaced over the past few years with lots of great features.  I built an engine called `StringTemplate` that not only has many handy features but that strictly enforces separation of model and view.

The trick is to provide sufficient power in a template engine without providing constructs that allow separation violations.  After examining hundreds of template files used in my web sites, I conclude that one only needs four template constructs:

1. attribute reference
1. conditional template inclusion based upon presence/absence of an
attribute
1. recursive template references
1. template application to a multi-valued attribute similar to lambda
functions and LISP's `map` operator

This discussion is pulled almost verbatim from my formal treatment of [strict model-view separation in template engines](http://www.cs.usfca.edu/~parrt/papers/mvc.templates.pdf).

