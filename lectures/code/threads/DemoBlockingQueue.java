import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Demo Java's BlockingQueue.
 *
 * send Integer like RingBuffer
 * jdk 1.6 N = 8000000		1671.234ms 4786882.0 events / second
 * jdk 1.7 N = 8000000		1944.089ms 4115037.8 events / second
 */
public class DemoBlockingQueue {
	public static final int N = 8000000;
	static Object EOF = new Object();

	static BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(1024);

	static class Producer implements Callable<Void> {
		public Void call() {
			for (int i = 1; i<=N; i++) {
				try {
					queue.put(i);
				}
				catch (InterruptedException ie) {
					ie.printStackTrace(System.err);
				}
			}
			queue.add(EOF);
			return null;
		}
	}

	static class Consumer implements Callable<Void> {
		@Override
		public Void call() throws Exception {
			try {
				Object o = queue.take();
				while ( o != EOF ){
					o = queue.take();
					//System.out.println(s);
				}
			}
			catch (InterruptedException ie) {
				ie.printStackTrace(System.err);
			}
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		List<Callable<Void>> jobs = new ArrayList<Callable<Void>>();

		Producer p = new Producer();
		Consumer c = new Consumer();
		jobs.add(p);
		jobs.add(c);

		long start = System.nanoTime();
		pool.invokeAll(jobs);
		pool.shutdown();
		pool.awaitTermination(2, TimeUnit.SECONDS);
		long stop = System.nanoTime();
		long t = stop - start;
		double tms = t / 1000.0 / 1000;
		double ts = t / 1000.0 / 1000 / 1000;
		System.out.printf("%.3fms %.1f events / second\n", tms, (N/(float)ts));
	}
}
