Software Patterns
====

Introduced around 1995 by "gang of four": _Design Patterns_ by Erich Gamma, Richard Helm, Ralph Johnson, and John Vlissides.

Organizing your code (the right classes and the right code in those classes) is a lifelong learning experience and it is very difficult for anything but the most simple programs.  For example, where should the code for operation "get email for user" go?  It could reasonably be placed either "functionally" in an EmailManager or "logically" in a User object.  I often compromise by placing things by functionality such as in the EmailManager and then creating a delegating function such as User.getEmail() that just calls the EmailManager.  I can therefore satisfy grouping by both functionality and by logic.

Experience with partitioning and organizing a program is particularly important when working with others on a large program.  It is specifically the code organization that supports code reuse, making your code smell better and last longer.  Using established patterns not only provides solutions you may not have thought of, but they are usually reusable out of the box.

Patterns are essentially just formalisms and the naming of nice idioms or patterns of coding.  Can also formalize design tactics or strategies.  A way to give experience to programmers (junior and senior).  A nice way to know you're talking about the same thing.  Provides high bandwidth conversation.  Most common patterns I've heard are _visitor_, _iterator_, _delegate_, _factory_, _proxy_.  The "gang of four" likens patterns to plot patterns in books such as "_tragically-flawed hero_".

Some people go nuts with these things and say "heh, you're not using pattern blort."  Most of the time you have a pattern in your head from prior experience and it just happens to fit in a particular situation.  Many people read "gang of four" book repeatedly hoping to gain the wisdom of ages.  I suspect that simply getting more coding experience and learning patterns in a specific situation from a more experienced developer will yield the best results.  I recommend against designing a program by sticking in patterns.  Patterns are not meant to be straightjackets.

Don't start with a pattern and think about how you can fit it into your application. Design your application and then, as part of the process, shift your implementations to fit a known pattern if it is close. That will make it easier for programmers to understand your code.  Patterns should not be where you start; they are an observation about well-written code.

To this day, I am learning new programming patterns.  I just don't try to look them up in a book so I can find its name; the exact variant may not actually have a name.  Design the program as you think about it, but recognize patterns when you see them.

Remember that empirically, we don't know the answer yet to building large software so any claims that patterns or XP or UML or whatever are the answer are suspect.  Also note that I programmed for 15 years before the patterns book came out without the need for calling a visitor a visitor.  William Shakespeare had it right:

  "_What's in a name? That which we call a rose / By any other name would smell as sweet._"

This lecture just lists the various popular patterns.

# A small beginning 

Here is the first pattern I learned by watching another person code.  Instead of

```java
int x = 0;
if ( cond ) {
  x = 3;
}
else {
  x = 4;
}
```

use

```java
int x = 4;
if ( cond ) {
  x = 3;
}
```

Depending on the CPU architecture / language / compiler, this might be faster or slower.  Regardless, I thought it was "tighter".

# Visitor


http://128.42.6.89/JavaResources/DesignPatterns/VisitorPattern.htm:

  "The purpose of the Visitor Pattern is to encapsulate an operation that you want to perform on the elements of a data structure. In this way, you can change the operation being performed on a structure without the need of changing the classes of the elements that you are operating on. Using a Visitor pattern allows you to decouple the classes for the data structure and the algorithms used upon them."

http://en.wikipedia.org/wiki/Visitor_pattern

If I'm walking a tree, I would provide an object to the tree walker that knew to call a method per tree node like an event call back.

This is also useful for parse trees and similar data structures. The idea is to create a Visitor class that has `visit` methods.

![](figures/stat-visitor.png)

Visitors are not so great at providing context information; that is, visitors usually apply an action to a single element, node, or atom of the structure without concern for surrounding elements.  For example, imagine you have a tree data structure representing programming language expressions like this:

```
  *
 / \
0   +
   / \
  3   4
```

A visitor pattern that does the addition should not bother as the surrounding context indicates the result must be 0 because of the multiplication by 0 around it.

One of the ugly things with this pattern is that you need a hook that does a _double-dispatch_ within the tree node classes. The double-dispatch method redirects `visit()` calls on a node to an appropriate method in a visitor servicing that node type. The visitor is like a set of callback methods. To make this work, we need to define the crucial `visit()` double-dispatch method signature in our generic node:

```java
public abstract class MathNode {
  public abstract void visit(VecMathVisitor visitor); // dispatcher
}
```

Unfortunately, we need to implement `visit()` in every class. Surprisingly, every visit() method is identical. Here it is in `AddNode` subclass of `MathNode`:

```java
public void visit(VecMathVisitor visitor) { visitor.visit(this); }
```

# Iterator

A form of visitor that walks a data structure, retrieving the elements one by one.  In Java, you will see a lot of code like this:

```java
List users = new LinkedList();
for (Iterator it = users.iterator(); it.hasNext();) {
    User u = (User) it.next();
    ...
}
```

# Factory

OO languages have great flexibility at the method call level (polymorphism).  You can refer to several types at once and send a "message."  At the creation side, however, you have to say `new T()` where `T` is a specific type.  Often a _factory_ is useful so you ask for a new something but the factory decides precisely what type.  For example, it could start using a debugging object:

```java
class UserFactory {
  public static User create() {
    if ( debug ) {
      return new DEBUG_User();
    }
    else {
      return new User();
    }
  }
  ...
}
```

Then at the reference site you say:

```java
User u = UserFactory.create(); // either User or DEBUG_User returned
```

This lets the reference site stay the same, but does not let you easily install a new factory so completely new User object creation functionality can be switched in.  For that, see the next section.

# Switch

The factory shown in previous pattern uses a switch (a "knob") to return different objects.  The knob is the "debug" variable and the IF-statement.

Adding a level of indirection is a very common computer science trick.  Putting in a switch between a reference site and a service provides lots of flexibility to swap out / in new functionality w/o changing your reference site.

# Delegation

Delegation is like inheritance done manually (because Java does not formalize the notion).  Delegation is the idea that an object answers a message but actually defers or delegates that functionality to a contained or referenced object.  This is very common.  For example,

```java
class Person {
  PersonManager pm = ...;
  public int getMembership() {
    return pm.getMembership(this.getID());
  }
  ...
}
```

The servlet `DispatchServlet` we discussed is an example of a delegator also because it delegates all functionality for a URL to a `Page` object.  The servlet is only the glue that delegates execution from the web server to your application code.

Why do we care about delegation?  Sometimes the relationship you want between pieces of data is one of containment not inheritance; combining into a single aggregate with a nice, well-defined interface is useful; you just delegate the functionality to other objects.

Delegates allow you to group code in multiple ways.  For example, in the above a `Person` should really be able to answer questions about it's membership, but do you really want all that complicated membership stuff in the `Person` object?  Probably not--it is better to have the `Person` object delegate the functionality to a proper service object, which groups all membership related functionality.


# Proxy

The factory pattern above creates normal `User` objects or debugging objects, `DEBUG_User` according to a debugging flag.  You call the `DEBUG_User` object a _proxy_ (in this case a _debugging proxy_).

More commonly you will see _remote proxies_ where, for example, a database manager is actually a proxy that communicates with a remote database.

You can add a _caching proxy_ that provides a buffer between you and a slow resource like a disk drive.  The `BufferedStream` is an example of a _caching proxy_.  It looks like a stream, but adds a cache.

A proxy is like a "tunnel".  For example, HTTP proxies are used by large companies to filter web access.  Users within the intranet set up their webservers to use a proxy.  When they access a URL, it actually first goes to the proxy which checks to see if the URL is valid.  If so, the proxy gets the data and then passes it back to the browser.

Let's look at a specific example of a proxy.  Imagine you have a program that fires "events" when things of interest occur.  You can set up an interface that is a "listener" such as this:

```java
interface DebugEventListener {
    public void outOfMemory();
    public void launchOfNewThread(Thread t);
    public void login(User u);
}
```

Then your program can create a listener:

```java
class MyMainApplication {
   class Tracer implements DebugEventListener {
       public void outOfMemory() {...}
       public void launchOfNewThread(Thread t) {...}
       public void login(User u) {...}
   }

   public static DebugEventListener listener = new Tracer();

   void startup() {...}

   void loginUser(User u) {
       if (...) listener.login(u);
   }
}
```

What if you want to remotely track events?  No problem.  Change 

```java
public static DebugEventListener listener = new Trace();
```

to create a new kind of listener that marshalls events across a socket and the `loginUser()` method will not have to change at all! 

```java
public static DebugEventListener listener = new RemoteSocketTrace("foo.com", 8080);
```

The listener is now a remote socket proxy that communicates with the real listener on foo.com.

# Adaptor

An adaptor makes one object look like another. It's like a proxy that alters the interface. The adaptor usually sits between a library that makes predefined method calls (such as callbacks from a GUI library) and user code that must answer those calls. Rather than structure your user code the way the library needs, structure the code the way you want and make an adaptor that routes the library method calls to your user code.

Adaptors are particularly useful when you need to make two pieces of software work together that are already written and cannot be changed.

# Cache

Here is a simple cache design that only loads a User object once from the database. Is a write-through cache in the sense that addUser writes to both the cache and the database.

```java
class DBMgr {
    Map<Integer,User> users = new HashMap<Integer,User>();
    public User getUser(int ID) {
        User u = users.get(ID);
        if ( u!=null ) return u;
        ... load user ID from db, put in u ...
        users.put(ID, u);
        return u;
    }
    public int addUser(User u) { // returns ID
        users.put(ID, u); // add (replace if there already)
        ... insert user into DB, get autoinc ID back ...
        return ID;
    }
}
```

# Singleton

When you have a collection of related methods, you can stuff them into a "module", making them `static`.

```java
public class MembershipManager {
    public static int getMembership(int ID) {...}
    ...
}
```

Useful to encapsulate.  However, it makes it hard to swap out that functionality since everything references `MembershipManager.getMembership(...)`.  Plus sometimes you need shared data between methods.

The _singleton_ pattern is useful for this.  Then you do:

```java
MembershipManager mm = MembershipManager.instance();
int m = mm.getMembership(...);
```

Naturally you really need an `interface` if you plan on swapping out functionality.

```java
public class MembershipManager {
    private static MembershipManager _instance = null;

    public static synchronized MembershipManager instance() throws Exception {
        if ( _instance == null ) {
            _instance = new MembershipManager();
        }
        return _instance;
    }

    /** Only this class can make new instance and it does it once. */
    private MembershipManager() {
    }

    /** non-static! */
    public int getMembership(int ID) {...}

    ...
}
```

# Command or operation

Often you need to record a sequence of operations so that you can undo them like a database supporting transactions.  GUIs often queue up _commands_ in an event queue that tell listeners what to do.

I recently used this pattern for operations on a buffer of strings.  This buffer needed to be an `ArrayList` for speedy access to the ith element.  However, after the buffer was created, I needed to insert strings and delete sequences of strings from say _i_ to element _j_.  Insert into an array is very expensive because you must move data and possibly reallocated the buffer.  By using a queue of commands instead of doing the actual operations, I was able to avoid the intensive work.  I just spit out the buffer later paying attention to the operations.  For example, to insert "foo" after string index 44, I simply recorded the operation and then, when printing, I add "foo" after printing the string at index 44.
