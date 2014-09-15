Java Thread Basics
====

# Overview

From: [Concurrency vs parallel execution](http://programmers.stackexchange.com/questions/190719/the-difference-between-concurrent-and-parallel-execution):

<blockquote><b>Concurrency</b> means that two or more calculations happen within the same time frame, and there is usually some sort of dependency between them. (Terence: GUIs have multiple things going on at the same time and are therefore concurrent but there might only be one processor so no parallelism.)

<p><b>Parallelism</b> means that two or more calculations happen simultaneously.

<p>Put boldly, concurrency describes a problem (two things need to happen together), while parallelism describes a solution (two processor cores are used to execute two things simultaneously).

<p>Parallelism is one way to implement concurrency, but it's not the only one. Another popular solution is interleaved processing (a.k.a. coroutines): split both tasks up into atomic steps, and switch back and forth between the two. (Terence: obviously timesharing on a single CPU is concurrent but not parallel)
</blockquote>


A thread is *not* an object, it's a series of executed instructions zipping thru method calls.  Imagine multiple CPUs and each one running code in your program (same data space) at the same time like ants crawling all over a code printout.  Java and the operating system take care of making one or a few CPUs handle many threads.  Here is the way I think of two threads executing methods `foo()` and `bar()` concurrently:

![](figures/thread.imagine.jpg)

but in reality on single CPU with a single core they are interleaved:

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

# Job pools

Works but no job control because `join()` only works on one thread:

```java
Thread t = new Thread(new Producer());
t.setName("Producer");
t.start();
t = new Thread(new Consumer());
t.setName("Consumer");
t.start();
```

Can't wait until we're done. Need `ExecutorService`:

```java
class Consumer implements Callable<Void> {
	public Void call()  { return null; }
}
...
ExecutorService pool = Executors.newFixedThreadPool(2);
List<Callable<Void>> jobs = new ArrayList<Callable<Void>>();

Consumer c = new Consumer();
Producer p = new Producer(c);
jobs.add(p);
jobs.add(c);

pool.invokeAll(jobs);
pool.shutdown();
pool.awaitTermination(2, TimeUnit.SECONDS);
```

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

Bank has &#36;100 in account.  Boss goes to other teller, you go to me as teller.  Both want to add 5&#36;.  Race condition.  No matter what, it's wrong value.  "test and set" operations must be synchronized.  Then note that if you record changes not new value: &#36;100, +5&#36;, +5&#36; then it's ok.  No test and set.  So, sync reqts depend on what you are doing.  Looks like

```java
class Account {
    double balance = 0.0;
    public void deposit(float value) { // UNSAFE!!!
        balance = balance + value;
    }
}
```

We can solve this with a sickness method and making balance private.

## Compound operations
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

See also client-side locking below.

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
  private double balance = 0.0;
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

Java uses the synchronized keyword not only for thread safety but also for synchronizing the execution of statements across threads and also for passing information between threads.

# Conditional synchronization

We want this:

```java
await (condition) do statement;
```

But we have `wait()` and `notifyAll()`. To call these functions you have to have a lock on the object because they inherently are waiting on state changes.

* `X.wait()`: releases the lock and suspends. Other threads can acquire the lock on `X`. Upon awakening, it requires the lock. The idea is to go to sleep until some event has occurred like data is available or I have completed the task.
* `X.notify()`: awaken a thread waiting on `X`'s lock.
* `X.notifyAll()`: awaken all threads waiting on `X`'s lock.

Note: `Thread.sleep(n)` does not release the lock as it does not have to be executed within a synchronized block. The sleep method is not a valid inter-thread communication; it just causes the current process for a while without consuming CPU time.

There might be lots of threads waiting on `X` for lots of different conditions. We cannot assume that we have been awakened for the proper condition and so it must be checked again. If we fail to find the event we wanted, we have to go back to sleep.  Goetz's concurrency book describes an example where lots of the being is going off in the kitchen; could be the microwave, the refrigerator door open, a cell phone, the oven, etc... Everyone wakes up to figure out if it's the condition they care about.

Instead of using `wait` and `notify`, we could use *busy waits* but those are typically very inefficient (not always...they are great if you need very low latency responses). See [SleepyBoundedBuffer.java](http://jcip.net.s3-website-us-east-1.amazonaws.com/listings/SleepyBoundedBuffer.java).

```java
    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
```


## Barrier wait

`t.join()` allows us to wait until `t` has finished, but what about having _n_ threads wait at a _barrier_ like this?  Let's implement our own barrier so that we can see how Java's library must do it.

![](figures/thread.barrier.jpg)

For example, you might want to queue n people for each bus.

Want to code like this:

```java
!INCLUDE "code/threads/ParallelComputation.java"
```

and this implementation

```java
!INCLUDE "code/threads/Barrier.java"
```

Here's a real example using job is built-in `CyclicBarrier`:

```java
!INCLUDE "code/threads/DemoCyclicBarrier.java"
```

Java API for `CyclicBarrier`:

<blockquote>
A synchronization aid that allows a set of threads to all wait for
 * each other to reach a common barrier point.  CyclicBarriers are
 * useful in programs involving a fixed sized party of threads that
 * must occasionally wait for each other. The barrier is called
 * <em>cyclic</em> because it can be re-used after the waiting threads
 * are released.
</blockquote>

# Inter-thread communication

## Producer "/" consumer model

Typically blocking on I/O and network traffic:

```java
/** Extend Queue to make threads block until remove has
 *  data.
 */
class MyBlockingQueue {
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
!INCLUDE "code/threads/SingleElementBlockingQueue.java"
```

Here is a `main()` that tests the queue:

```java
!INCLUDE "code/threads/DemoSingleElementBlockingQueue.java"
```

Note that I try to consume first.  It will wait for 2 seconds (2000 ms) before the producer starts up and adds the element.

# Java data structures

`java.util` classes list `ArrayList` and `HashMap` are not thread safe. Old classes like `Vector` and `Hashtable` are but slower.

Use `Collections.synchronizedXXX()` factories to make `ArrayList` and `HashMap` and friends thread-safe.

```java
!INCLUDE "code/threads/SingleElementBlockingQueue.java"
```

# Some recommendations, patterns

You should read [Java concurrency in practice](http://jcip.net.s3-website-us-east-1.amazonaws.com/).

* Try to avoid **shared state** between threads
* If that is not possible, use **immutable objects** if you can:
```java
public class Point {
    final int x,y;
	public Point(int x, int y) { ... }
}
```
* For mutable objects, make sure that **fields are not directly accessible** and all methods that **get and set data are synchronized**.
```java
// partially from Brian Goetz and Tim Peierls
public class Point {
    private final int x,y; // private!
	public Point(int x, int y) { ... }
    public synchronized int[] get() { return new int[]{x, y}; }
    public synchronized void set(int x, int y) { ... }
}
```
* Protect **test-and-set** operations like `account += value` and *if there is data, give me the next element*. `count++` is also not thread safe.
* Remember to **synchronize all read sites** not just write sites to the same state and using the same lock!
* Be careful you **do not publish data** that is not adequately protected by returning a data structure.
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
* **Lock as little as possible** and for a short of time as possible but no less. This is for liveness, simplicity, and speed.
* Either to overcome unsafe classes or to guard sequences of operations with safe classes that must be done together or not done at all, use **client-side locking**:
```java
// @author Brian Goetz and Tim Peierls
public class SafeVectorHelpers {
    public static Object getLast(Vector list) {
        synchronized (list) {                // client-side lock
            int lastIndex = list.size() - 1; // test
            return list.get(lastIndex);      // set
        }
    }
    public static void deleteLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1; // test
            list.remove(lastIndex);          // set
        }
    }
}
```
A thread could interrupt in between the `list.size()` and the `list.get()`.
* Serialize access using a single thread. GUI event threads are the most obvious example. Java's Swing library has the [Event Dispatch Thread](http://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html) do all of the manipulations on graphics objects. Multiple threads right to an event queue and there is a single thread that pulls work off of that queue. Very convenient in terms of thread safety but makes it slightly inconvenient when you need to react to events. If the event handler is a very expensive, the event thread must launch another thread to process it otherwise the entire GUI will freeze while the event is processed.

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
