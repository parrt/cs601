import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Demo Java's Actor msg passing.
 *
 * MAX_BUFFER_SIZE = 1024 -server
 * jdk 1.6 N = 8000000	Vector		4219.418ms 1895996.0 events / second
 * jdk 1.6 N = 8000000	List		4623.501ms 1730290.6 events / second
 * jdk 1.8 N = 8000000	List		5162.941ms 1549504.5 events / second
 */
public class DemoActor {
	public static final int N = 8000000;
	public static final int MAX_BUFFER_SIZE = 1024;
	static Object EOF = new Object();

	public static void main(String[] args) throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		List<Callable<Void>> jobs = new ArrayList<Callable<Void>>();

		ConsumerActor c = new ConsumerActor();
		ProducerActor p = new ProducerActor(c);
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
