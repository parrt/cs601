import java.util.concurrent.CyclicBarrier;

/** Map-Reduce addition example
 *
 *  Take data array of size N and split into SPLITS chunks.
 *  Launch a thread running an Adder on each chunk.
 *  The Adders right to partialResults array. There is no safety issue
 *  because they write to different positions in the array.
 *  Wait for all of them to reach the barrier.
 *  Reduce the results to a single some and print it out.
 */
public class DemoCyclicBarrier {
	public static final int N = 1000*1000*10;
	public static final int SPLITS = 10; // increasing this to 100 takes more time
	public static final int SPLIT_SIZE = N/SPLITS;
	static int[] data = new int[N];
	static long[] partialResults = new long[SPLITS];

	// +1 for main thread
	static final CyclicBarrier barrier = new CyclicBarrier(SPLITS+1);

	static class Adder implements Runnable {
		int split;
		public Adder(int split) {	this.split = split;	}
		public void run() {
			int start = split * SPLIT_SIZE;
			for (int i=0; i<SPLIT_SIZE; i++) {
				partialResults[split] += data[start+i];
			}
			System.out.println(Thread.currentThread().getName()+" done");
			try { barrier.await(); }
			catch (Exception e) { System.out.println("eh?"); }
		}
	}

	public static void main(String[] args) throws Exception {
		// init; make some data
		for (int i=0; i<N; i++) data[i] = i+1; // 1, 2, 3, 4, ..., N

		long start = System.currentTimeMillis();
		final Thread[] threads = new Thread[SPLITS];

		// create a thread on each split
		for (int i=0; i<SPLITS; i++) {
			threads[i] = new Thread(new Adder(i));
		}

		// MAP
		for (int i=0; i<SPLITS; i++) threads[i].start();

		barrier.await(); // wait for all threads

		// REDUCE
		long sum = 0;
		for (int i=0; i<SPLITS; i++) {
			sum += partialResults[i];
		}
		long stop = System.currentTimeMillis();

		// should be (1 + N)*(N/2) = 500500
		System.out.println("Sum is "+sum);
		System.out.printf("time %d ms\n", stop - start);
	}
}
