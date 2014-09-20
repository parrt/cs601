Message passing via blocking queues
=======

# Goal

Your goal in this project is to explore the use of blocking queues and message passing between producers and consumers or *actors*. You will build two blocking queues yourself, `SynchronizedBlockingQueue` and `RingBuffer`, then in another case you will use Java's `ArrayBlockingQueue`.

In order to test the efficiency of the blocking queues, you will also build a `ThreadObserver` that measures the ratio of time a thread spends block versus running.

This project requires the use of most of the thread concepts we discussed so far, including `synchronized` methods, `volatile` fields, lockless data structures, Java's memory model, spin locks, thread synchronization, and blocking data structures.  As a bonus, you will also have to be comfortable with generic types to use the code I have provided.

# Discussion

This project requires a message queue between a producer and a consumer, which we can formalize as follows:

```java
public interface MessageQueue<T> {
	void put(T o) throws InterruptedException;
	T take() throws InterruptedException;
}
```

Your repository has producer and consumer objects already defined.

I have also provided a test rig that creates a producer and consumer that communicate using the message queue and object sequence passed in. The number of messages is also required as an argument to the `test` function. 

```java
RingBuffer<Integer> queue = new RingBuffer<Integer>(1024);
MessageSequence<Integer> sequence = new IntegerSequence(1,N);
TestRig.test(queue, sequence, N);
```

You will notice that the message sequence has the notion of an end of file sentinel object. Without such a sentinel, we'd need control methods, which are significantly more complicated.

All of the message buffers will be of fixed size (1024). Do not allocate more and more space in the buffers as you get messages. The producer should block when that buffer is full and the consumer should block when that buffer is empty. The consumer finishes when it sees the end of file sentinel object.

The test rig launches `ThreadObserver` objects to track how much of the time our producer and consumers are blocked using a simple *sampling* technique. It's not perfect because, for example, if the computer is busy the overall time taken by the test will be extended. This would artificially reduce the ratio time our threads spent blocked.

The first thing you should do is read through all the source code that has been provided so you can fully understand the foundation I have provided. Some of you will have to go read about generic types of learning is part of the development process, as is reading other people's code.

# Tasks

## Message buffer using synchronization

You will find a skeleton for `SynchronizedBlockingQueue` in your repository.

You must implement `take` and `put` so that they block when the buffer is empty or the buffer is full, respectively.

## Message buffer using `ArrayBlockingQueue`

You will find a skeleton for `MessageQueueAdaptor` in your repository.

This is really just a wrapper or an adapter between an `ArrayBlockingQueue` and our `MessageQueue` interface. There is very little work to do here consequently but it gives you an idea of how fast it is in comparison to the other two implementations.

## Lockless message buffer

You will find a skeleton for `RingBuffer` in your repository.

Using a [circular buffer](http://en.wikipedia.org/wiki/Circular_buffer), sometimes called a *ring buffer*, you must create a lockless message buffer. For simplicity, your buffer need only be thread-safe for a **single** reader and **single** writer. We think of the buffer as circular because we start writing over elements at the beginning instead of falling off the end of the buffer. But, it's easier to think about linearly. Here's the initial state of the buffer for n=4:

<img src="figures/ring-init.png" width=330>

Where `r` is where we are *about to read* and `w` is where we have *just written*. Therefore, a full buffer is when `w` is at the end of the buffer and cannot wrap because we have not read any values yet:

<img src="figures/ring-full.png" width=405>

After we have read the single element, `w` can wrap to 0:

<img src="figures/ring-full-read-1.png" width=415>

Note that we are tracking absolute indexes, using `long`s, rather than keeping a pointer inside the array or an index in 0..n-1. This is convenient as it can tell us how many we have read and written but also is important for the lock plus nature of this data structure. `r` and `w` will constantly chase each other towards infinity as `volatile long`s.

```java
public class RingBuffer<T> implements MessageQueue<T> {
	// must be volatile as reader/writer threads share w, r
	private volatile long w = -1L;		// just wrote location
	private volatile long r = 0L;		// about to read location
	...
	public RingBuffer(int n) { ... }
}
```

Note: you should throw an `IllegalArgumentException` if the fixed buffer size passed to the constructor is not power of 2.  If `n % k` is `n & (k-1)` if n power of 2, which is vastly more efficient than the mod operator. Here is a useful function for you:

```java
// http://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetParallel
static boolean isPowerOfTwo(int v) {
	if (v<0) return false;
	v = v - ((v >> 1) & 0x55555555);                    // reuse input as temporary
	v = (v & 0x33333333) + ((v >> 2) & 0x33333333);     // temp
	int onbits = ((v + (v >> 4) & 0xF0F0F0F) * 0x1010101) >> 24; // count
	// if number of on bits is 1, it's power of two, except for sign bit
	return onbits==1;
}
```

This data structure is straightforward if we don't care about blocking or threads. The key comes down to two methods that I have in my implementation.

```java
// spin wait instead of lock for low latency store
void waitForFreeSlotAt(final long writeIndex) {
	// wait until we have at least one spot, meaning w < r
	// since circular buffer though we worry about wrapping. We
	// have to wait if we've got n values in the buffer already.
}
```

```java
// spin wait instead of lock for low latency pickup
void waitForDataAt(final long readIndex) {
	// wait until w catches up or passes desired read location
	// repeat until just-wrote-index >= about-to-read-index
}
```

These methods actually require a bit of a delay in their spins otherwise overall throughput suffers. I suspect this is because the spin loop saturates the data bus because it constantly accesses volatile fields `r` and `w`. In other words, both the reader and the writer threads are constantly pounding away trying to access two 64-bit longs in main memory instead of the cache.

## Thread observer

You must create an observer that watches another thread (our producer or consumer, in this case) and tracks how much of the time is spent blocked. It also has to track the methods found when the observer wakes up. Create a histogram using a `Map<String, Long>` whose key is exactly *classname*.*methodname* without the parentheses on the end.

The idea is to wake up at some frequency and figure out what the observed threads doing. You must ask the observed thread what state is (See `Thread.State` `enum`) and also ask the observed thread for it stack trace. The element on the top of the stack is the currently executing method.

Continue monitoring the thread until someone calls `terminate()` on the observer.

# Submission

All projects must reside within the appropriate *userid-blkqueue* repository at github and following the directory structure provided. As usual, I've provided a build script but of course you don't have to use that if you don't want. It will however be triggered when you commit things to the repository.

**Projects not developed using github/git will get 0 points.** Looking back at previous histories, it's clear many of you simply push right before the project is due and therefore I do not have a history of your commits. I must see the development of the software.

As you develop the software, you will run into infinite loops and deadlock. You should really be testing things on your own computer and trying to submit things only that kind of work or at least don't deadlock. That way the Travis server will not try to run your program forever. If it does seem to get stuck when you are looking at the log as it executes on Travis, you can hit the cancel button which looks like a big X. We don't want to bring down their servers, particularly when we are getting all this for free.