import com.oracle.jrockit.jfr.Producer;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Demo Java's BlockingQueue.
public class DemoBlockingQueue {
	static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
	static Object data = null;
	static Object semaphore = new Object();

	static class Producer implements Runnable {
		public void run() {
			while ( true ) {
				try {
					queue.put(String.valueOf(new Date().getTime()));
				}
				catch (InterruptedException ie) {
					ie.printStackTrace(System.err);
				}
			}
		}
	}

	static class Consumer implements Runnable {
		public void run() {
			while ( true ) {
				try {
					String s = queue.take();
					//System.out.println(s);
				}
				catch (InterruptedException ie) {
					ie.printStackTrace(System.err);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Thread t = new Thread(new Consumer());
		t.setName("Consumer");
		t.start();
		t = new Thread(new Producer());
		t.setName("Producer");
		t.start();
	}
}
