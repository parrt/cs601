/** Simple queue that holds single value */
class BlockingQueue {
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
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }
}
