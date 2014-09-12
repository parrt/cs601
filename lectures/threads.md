Java Thread Basics
====

# Overview

A thread is *not* an object, it's a series of executed instructions zipping thru method calls.  Imagine multiple CPUs and each one running code in your program (same data space) at the same time like ants crawling all over a code printout.  Java and the operating system take care of making one or a few CPUs handle many threads.  Here is the way I think of two threads executing methods `foo()` and `bar()`:

![](figures/thread.imagine.jpg)

but in reality on single CPU:

![](figures/thread.context.switch.jpg)

Threads are useful because:

* support concurrent operation.  HTTP server.  crontab system (restarts managers, does stats, etc...).
* better system response. Add forum message; must add to search engine, email monitors.
* exploit parallelism. dual server: supports simultaneous execution
* Threads often permit simple programs.  Sequential loop doing bits of this and that, but that is a scheduler.  Don't have to check all the time for user events.

# Launching threads

1. Create class implementing `Runnable`.
1. Define `public void run()` method.  Thread dies when it exits.
1. Elsewhere, create instance of class, `r`.
1. Create `new Thread(r)`, `t`, attached to `r`.
1. `t.start()`.

Do Jabber (like log server).  Show lines of execution with interleaving.

```java
class Jabber implements Runnable {
  String str;
  public Jabber(String s) { str = s; }
  public void run() { // thread dies when finished
    while (true) {
      System.out.print(str);
      System.out.println();
    }
  }
}
```

Can use via:

```
class JabberTest {

  public static void main(String[] args) {
    Jabber j = new Jabber("University of San Francisco");
    Jabber k = new Jabber("Computer Science 601");
    Thread t = new Thread(j);
    Thread u = new Thread(k);
    t.start();
    u.start();
  }
}
```

Should intermix and not sync per line.

# Thread control

* `join()` wait for thread to finish
```
Computation c = new Computation(34);
Thread t = new Thread(c);
t.start();
t.join();
System.out.println("done");
```
* `sleep(int n)` sleep for n ms (keep locks) 
* `interrupt()` send signal to interrupt a sleeping or waiting thread
* `yield` suggest another can run

Here is a modified `Jabber` that forces a yield every 5 prints:

```
public class Jabber implements Runnable {
    String str;
    int i = 0;
    public Jabber(String s) { str = s; }
    public void run() {
        while (true) {
            i++;
            if ( i%5==0 ) {
                Thread.yield();
            }
            System.out.print(str);
            System.out.println();
        }
    }
}
```

Auto yields when blocked on IO.

Here is the thread lifecycle:

![](http://flylib.com/books/4/27/1/html/2/images/14fig06.jpg)

# Synchronization, Interference

## Shared resource example

"critical section": Trains on same track (semaphore term comes from this).

jGuru server pages: JSP share a single page object.  Threads were stepping on the dynamic computations and output yielding bizarre stuff like the user name of one page would appear on somebody elses.  

```html
<html>

<body>

<%! User u = getUser(); %> <!-- defines instance var -->

...

Hello <%= u.getName() %>!!!

</body>

</html>
```

To solve: I elected to have each page ref generate an object.  GC can handle this pretty well.  Plus I use a cache to avoid unnecessary computation.


## Race condition example

Bank teller issue; get a student to be other teller.  Ask them to look at board where you have $100 in account.  Boss goes to other teller, you go to me as teller.  Both want to add 5$.  Race condition.  No matter what, it's wrong value.  "test and set" operations must be synchronized.  Then note that if you record changes not new value: $100, +5$, +5% then it's ok.  No test and set.  So, sync reqts depend on what you are doing.  Looks like

```java
class Account {
  float balance = 0.0;
  public void deposit(float value) {
    balance = balance + value;
  }
}
```

## Monitors

Java's thread model based upon monitors: chunks of data accessed only thru set of mutually exclusive accessor routines.  We can this an object:

```java
class Data {
      // ... elements ...
      public synchronized void insert(Object o) {...}
      public synchronized void delete(String key) {...}
      public synchronized void read(String key) {...}
}
```

A `synchronized` method acquires a lock on the object (not the class).

Protects methods exec not data.

What happens when another thread interrupts and calls deposit?  Solution:

```java
class Account {
  float balance = 0.0;
  public synchronized void deposit(float value) {
    balance = balance + value;
  }
}
```

Like a "force field" around object.

*Note*: Can lock statements too with `synchronized (object) statement`.

*Note*: Class methods can be synchronized also:

```
class HPLaser {
  private static Device dev = ...;
  public static synchronized void print(String s) {...}
}
```

*Note*: Assignments are atomic minus `long` and `double`.

*Note*: local variables cannot be shared between threads so can't interfere.

# Java Memory Model

Jeremy Manson's [What Volatile Means in Java](http://jeremymanson.blogspot.com/2008/11/what-volatile-means-in-java.html)

## Issues with synchronization

[Double Checked Locking](http://jeremymanson.blogspot.com/2008/05/double-checked-locking.html)

[Double-Checked Locking is Broken](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)

```java
// Correct multithreaded version
class Foo { 
  private Helper helper = null;

  public synchronized Helper getHelper() {
    if (helper == null) 
        helper = new Helper();
    return helper;
  }
  // other functions and members...
}
```

```java
class Foo {
        private volatile Helper helper = null; // MUST be volatile

        public Helper getHelper() {
            if (helper == null) {
                synchronized(this) {
                    if (helper == null)
                        helper = new Helper(); // Helper init could happen out of order
                }
            }
            return helper;
        }
}
```


## Conditional synchronization and inter-thread communication

Want 

```java
await (condition) do statement;
```

Have `wait()` and `notifyAll()`.

Producer "/" consumer model such as blocking on I/O:

```java
/** Extend Queue to make threads block until remove has
 *  data.
 */
class BlockingQueue {
   int n = 0;
   Queue data = ...;
   public synchronized Object remove() {
      // wait until there is something to read
      while (n==0) this.wait();
      // we have the lock and state we're seeking
      n--;
      // return data element from queue
    }
    public synchronized void write(Object o) {
      n++;
      // add data to queue
      // have data.  tell any waiting threads to wake up
      notifyAll();
    }
}
```

Why is remove synchronized?  It's destructive; must be critical section.

Why write synchronized?  Critical section.

Here is an implementation of a 1-element queue:

```java
/** Simple queue that holds single value */
class BlockingQueue {
    int n = 0;
    Object data = null;
    public synchronized Object remove() {
        // wait until there is something to read
        try {
            while (n==0) wait();
        }
        catch (InterruptedException ie) {
            System.err.println("heh, who woke me up too soon?");
        }
        // we have the lock and state we're seeking; remove, return element
        Object o = this.data;
        this.data = null; // kill the old data
        n--;
        return o;
    }

    public synchronized void write(Object o) {
        n++;
        // add data to queue
        data = o;
        // have data.  tell any waiting threads to wake up 
        notifyAll();
    }
}
```

Here is a `main()` that tests the queue:

```java
class BlockingQueueTest {
    static class Producer implements Runnable {
        public void run() {
            q.write("hello");
        }
    }

    static class Consumer implements Runnable {
        public void run() {
            System.out.println("data is "+q.remove());
        }
    }

    static BlockingQueue q;
    public static void main(String[] args) throws Exception {
        q = new BlockingQueue();
        new Thread(new Consumer()).start();
        Thread.sleep(2000);
        new Thread(new Producer()).start();
    }
}
```

Note that I try to consume first.  It will wait for 2 seconds (2000 ms) before the producer starts up and adds the element.

*Barrier wait example*.  `t.join()` allows us to wait until `t` has finished, but what about having _n_ threads wait at a _barrier_ like this?  

![](figures/thread.barrier.jpg)

For example, you might want to queue n people for each bus.

Want to code like this:

```java
class ParallelComputation implements Runnable {
    public void run() {
        // DO SOME COMPUTATION
		// now wait for others to finish
        try {
            Main.barrier.waitForRelease();
        }
        catch(InterruptedException e) {}
    }
}

public class Main {
    public static Barrier barrier = new Barrier(3);
    public static void main(String[] args) {
        new Thread(new ParallelComputation()).start();
        new Thread(new ParallelComputation()).start();
		// if you comment this one out, program hangs!
        new Thread(new ParallelComputation()).start();
    }
}
```

and this implementation

```java
/**A very simple barrier wait.  Once a thread has requested a
 * wait on the barrier with waitForRelease, it cannot fool the
 * barrier into releasing by "hitting" the barrier multiple times--
 * the thread is blocked on the wait().
 */
public class Barrier {
    protected int threshold;
    protected int count = 0;

    public Barrier(int t) {
        threshold = t;
    }

    public void reset() {
        count = 0;
    }

    public synchronized void waitForRelease()
        throws InterruptedException
    {
        count++;
        // The final thread to reach barrier resets barrier and
        // releases all threads
        if ( count==threshold ) {
            // notify blocked threads that threshold has been reached
            action(); // perform the requested operation
            notifyAll();
        }
        else while ( count<threshold ) {
            wait();
        }
    }

    /** What to do when the barrier is reached */
    public void action() {
        System.out.println("done");
    }
}
```

# Starvation

A thread with higher priority preempts your thread, never allowing it any CPU time. In Java, the thread with the highest priority is running, implying that any thread at a lower priority is starved unless the higher priority thread blocks or waits.

# Deadlock

Unsatisfied wait condition.  Use HTTP project as an analogy (server must consume all headers from browser after the GET before sending result back or browser may hang.  It's not looking for the response yet as it's waiting for the server to read all the headers).

Or, I'm waiting on you and you're waiting on me.  Jim was waiting on me to tell him when I finished something and I was waiting on him.

Dining philosophers: think and eat.  Get 2 students and some knives or chopsticks from the cafeteria.  Must have two to eat but only one in front of you.  Everybody grabs to the right and then waits for stick on left.  Deadlock.  If not available, wait until it is then eat.  Get a cookie for student volunteers.  One possibly solution is to have one philospher as a nonconformist: grabs left first.  If they are greedy they starve.  Could also have the grabbing of two sticks be synchronized so at least the first n-1 guys guy will finish.

## Avoidance

No universal solution.  Redflags:

* watch out when x<->y and access methods are synchronized. 
* watch out for unsatisfied conditions.
* watch out for lots of threads competing for limited resources.
