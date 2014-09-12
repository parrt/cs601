import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collections;

// Demonstrate the use of a random object as a semaphore
public class PlayCatch {
	static Object data = null;
	static Object semaphore = new Object();

	static class TheThrower implements Runnable {
		public void run() {
			data = "hello";
			synchronized (semaphore) {
				semaphore.notify();
			}
		}
	}

	static class TheCatcher implements Runnable {
		public void run() {
			try {
				synchronized (semaphore) {
					while ( data==null ) semaphore.wait();
				}
			}
			catch (InterruptedException ie) {
				System.err.println("leave me alone");
			}
			System.out.println("data is "+data);
		}
	}

	public static void main(String[] args) throws Exception {
		new Thread(new TheCatcher()).start();
		Thread.sleep(10);
		new Thread(new TheThrower()).start();
	}
}
