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
