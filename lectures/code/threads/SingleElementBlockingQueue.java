/** Simple queue that holds single value */
class SingleElementBlockingQueue {
    private volatile int n = 0;
    private volatile Object data = null;
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
		notifyAll();
		return o;
    }

    public synchronized void write(Object o) {
		// wait until there is room to write
		try {
			while ( n==1 ) wait();
		}
		catch (InterruptedException ie) {
			throw new RuntimeException("woke up", ie);
		}
        n++;
        // add data to queue
        data = o;
        // have data.  tell any waiting threads to wake up
        notifyAll();
    }
}
