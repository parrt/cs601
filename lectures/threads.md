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

# Thread safety issues

*Given shared state among multiple threads, they are safe as long as only read operations are performed on that state. If even one of them must write to it, they must coordinate read/write operations to be safe.*

We use `synchronized`, but also the `volatile` keyword as well as elements from `java.util.concurrent`.

A *race condition* occurs when the result depends on the order in which operations between threads are executed. This is not necessarily bad as in games that interact with the user but it generally is bad.

## Shared resource example

"critical section": Trains on same track (semaphore term comes from this).

Most common: The following terms all mean the same thing compare-and-set, test-and-set, check-then-act, read-modify-write. You check on some condition and then act on it but by the time you act, the condition might be changed. This can happen if you look both ways before crossing the street and then blindly walk; by the time you get into the road a car might be coming around the corner.  You might check to see if a file exists and then tried to delete it but another thread might get deleted before you perform the deletion.

## Lazy initialization example

```java
// @author Brian Goetz and Tim Peierls
public class LazyInitRace { // UNSAFE!!!!
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null) { // two threads might reach this conclusion simultaneously
            instance = new ExpensiveObject();
		}
        return instance;
    }
}
```

We can solve by supervising this method or by synchronizing just the comparison block as we will see below with "Double checked locking".

## Improper banking example

Bank has $100 in account.  Boss goes to other teller, you go to me as teller.  Both want to add 5$.  Race condition.  No matter what, it's wrong value.  "test and set" operations must be synchronized.  Then note that if you record changes not new value: $100, +5$, +5% then it's ok.  No test and set.  So, sync reqts depend on what you are doing.  Looks like

```java
class Account {
    double balance = 0.0;
    public void deposit(float value) { // UNSAFE!!!
        balance = balance + value;
    }
}
```

We can solve this with a sickness method and making balance private.

# Monitors

Java's thread model based upon monitors: chunks of data accessed only thru set of mutually exclusive accessor routines.  We can model this an object:

```java
class Data {
      // ... elements ...
      public synchronized void insert(Object o) {...}
      public synchronized void delete(String key) {...}
      public synchronized void read(String key) {...}
}
```

A `synchronized` method acquires a lock on the object (not the class). Every instance possesses a lock; atomic elements like integers can only be locked by locking some object, perhaps an enclosing one.

*Locking on an array object does not prevent multiple methods from accessing
 the elements of the array.*

Protects methods execution not data.

What happens when another thread interrupts and calls `deposit()`?  Solution:

```java
class Account {
  float balance = 0.0;
  public synchronized void deposit(float value) {
    // lock on 'this' object acquired
    balance = balance + value;
	// lock released
  }
}
```

Like a "force field" around object. 

* Lock is released upon exit from a synchronized method, even upon exception.
* If multiple threads try to access the same method on the same object, one succeeds and the others wait to acquire the lock.
* Non-synchronized methods do not respect the lock and can execute despite another thread having locked the object.

*Note*: Can lock statements too with `synchronized (object) statement`.

Same as

```java
public void deposit() { synchronized(this) { ... } }
```
*Note*: Class methods can be synchronized also:

```
class HPLaser {
  private static Device dev = ...;
  public static synchronized void print(String s) {...}
}
```

*Note*: Assignments are atomic minus `long` and `double`.

*Note*: local variables cannot be shared between threads so can't interfere.

# Java data structures

`java.util` classes list `ArrayList` and `HashMap` are not thread safe. Old classes like `Vector` and `Hashtable` are but slower.

Use `Collections.synchronizedXXX()` factories to make `ArrayList` and `HashMap` and friends thread-safe.

Compound operations are not thread-safe even when using thread-safe data structures. Protect code sequences that perform multiple operations that cannot be interrupted.

```java
class Amazon {
	private List<Book> inventory =
		Collections.synchronizedList(new ArrayList<Book>());
	private List<Sale> sales =
		Collections.synchronizedList(new ArrayList<Sale>());
	...
	public synchronized void checkout(Book b) {
		inventory.remove(b);        // op A
		sales.add(new Sale(b));     // op B
	}
	public synchronized Book audit() {
	    // check that all books are accounted for
		// This would give false "missing book" if we interrupt
		// checkout() between A and B.
	}
}
```
Note that while A and B operations are themselves atomic, we must declare the entire checkout procedure as a critical section via `synchronized` that must not be interrupted.


# Conditional synchronization and inter-thread communication

We want this:

```java
await (condition) do statement;
```

But we have `wait()` and `notifyAll()`.

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
# Some recommendations, patterns

You should read [Java concurrency in practice](http://jcip.net.s3-website-us-east-1.amazonaws.com/).

* Use immutable objects one possible
```java
public class Point {
    final int x,y;
	public Point(int x, int y) { ... }
}
```
* For mutable objects, make sure that fields are not directly accessible and all methods that get and set data are synchronized.
```java
// partially from Brian Goetz and Tim Peierls
public class Point {
    private final int x,y; // private!
	public Point(int x, int y) { ... }
    public synchronized int[] get() { return new int[]{x, y}; }
    public synchronized void set(int x, int y) { ... }
}
```
* Protect test-and-set operations like `account += value` and *if there is data, give me the next element*. `count++` is also not thread safe.
* Remember to synchronize all read sites not just write sites to the same state and using the same lock!
* Be careful you do not publish data that is not adequately protected by returning a data structure.
```java
class T {
	static double secret = 99.0; // multiple threads can see this
	private SomeMutable[] internal = ... ;
	// synchronize does nothing here as we publish all data
	public synchronized SomeMutable[] getValues() { return internal; }
	// synchronize does nothing here as we publish mutable object
	public synchronized SomeMutable getValue(int i) { return internal[i]; }
}
```
* Lock as little as possible and for a short of time as possible but no less. This is for liveness, simplicity, and speed.

# Making shared data visible across threads

*At this point, everything is just kind of work, but there is a hidden gotcha that we must ensure that data written in one thread is visible to another thread*.

Synchronization is not just about synchronizing threads. It's also about making sure that threads can see data written by other threads. As Goetz et al pointed out in the Java concurrency book, it seems natural that thread X writing to field `salary` that thread Y would see the changed value if it occurs after the write operation. Without synchronization and/or `volatile`, this is not the case.  From Jeremy Manson, assume the following `ready` variable is `volatile`:

![](http://farm4.static.flickr.com/3073/3035268779_f8a9dce89d.jpg)

Because ready is volatile, the second thread is guaranteed to print 42. When thread 1 writes to ready and then the second thread reads ready, all data previously written by thread 1 becomes visible to thread 2. Without the volatile, it might be the case that just one of those variables could leak through. For example, if ready leaks through but answer does not, the second thread will print 0!

To make threads operate efficiently on multi-core machines and to allow compiler optimizations that reorder operations, the Java memory model has to allow thread to pretend to execute in its own sandbox (which allows the CPU running the thread to access data from its local cache and not have to go all went back to main memory). [JDK5 and later extends the semantics for volatile so that the system will not allow a write of a volatile to be reordered with respect to any previous read or write, and a read of a volatile cannot be reordered with respect to any following read or write. ](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)

Jeremy Manson's [What Volatile Means in Java](http://jeremymanson.blogspot.com/2008/11/what-volatile-means-in-java.html)

Synchronization is also a means to ensure data written in one thread is available to another thread. An unlock on O in thread X followed by a lock of O in Y, guarantees that all data written prior to the unlock by X is visible to thread Y.

Manson: "*The reason that doesn't work for (non-volatile) double-checked locking is that only the writing thread ever performs the locking.*"

Before learning all of these details, I never had a problem with data not being communicated across threads. It could be that I just got lucky but I suspect it's because I had all of my shared state synchronized properly and synchronization makes data visible (and prevents compiler optimizations that would cause problems).

Volatile variables are slower because they cannot be cached and it prevents compiler optimizations that perform reordering. On the other hand volatile variables cannot cause deadlock because of locking as they are not locks

Goetz example demonstrating that volatile variables are good for status flags that indicate when to exit loops:

```java
volatile boolean asleep;           // set by thread X
...
   while ( !asleep ) countSheep(); // read by thread Y
```

## Double checked locking

[Double-Checked Locking is Broken](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)

The following is a correct but less than optimally efficient mechanism to get a singleton `Helper` object:

```java
// Correct multithreaded version
class Foo {
  private Helper helper = null;

  public synchronized Helper getHelper() {
    if (helper == null) {
        helper = new Helper();
	}
    return helper;
  }
  // other functions and members...
}
```

There will be locking for every reference to `getHelper()` even though we no longer need to lock because the object has been created.

We can try to make it more efficient by doing this

```java
// Broken -- Do Not Use!
class Foo {
  private Helper helper = null;
  public Helper getHelper() {
    if (helper == null) {
      synchronized(this) {
        if (helper == null) {
          helper = new Helper();
        }
      }
    }
  return helper;
}
```

```java
class Foo {
	private volatile Helper helper = null; // MUST be volatile

	public Helper getHelper() {
		if (helper == null) {
			synchronized(this) {
				if (helper == null) {
					// Helper init could happen out of order, i.e., after
					// helper field has been written.
					helper = new Helper();
				}
			}
		}
		return helper;
	}
}
```

Notice that if were using static singletons, [Java semantics guarantee that we won't see anything strange happened](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html):

```java
class HelperSingleton {
  static Helper singleton = new Helper();
}
```


# Starvation

A thread with higher priority preempts your thread, never allowing it any CPU time. In Java, the thread with the highest priority is running, implying that any thread at a lower priority is starved unless the higher priority thread blocks or waits.

# Deadlock

Unsatisfied wait condition.  Use HTTP project as an analogy (server must consume all headers from browser after the GET before sending result back or browser may hang.  It's not looking for the response yet as it's waiting for the server to read all the headers).

Or, I'm waiting on you and you're waiting on me.  Jim was waiting on me to tell him when I finished something and I was waiting on him.

Dining philosophers: think and eat.  Get 2 students and some knives or chopsticks from the cafeteria.  Must have two to eat but only one in front of you.  Everybody grabs to the right and then waits for stick on left.  Deadlock.  If not available, wait until it is then eat.  Get a cookie for student volunteers.  One possibly solution is to have one philospher as a nonconformist: grabs left first.  If they are greedy they starve.  Could also have the grabbing of two sticks be synchronized so at least the first n-1 guys guy will finish.
