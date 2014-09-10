import java.util.concurrent.CyclicBarrier;

// Demonstrate CyclicBarrier
public class ParallelCompute {
	public static final int N = 1000;
	public static final int SPLITS = 10;
	public static final int SPLIT_SIZE = N/SPLITS;
	static int[] data = new int[N];
	static int[] partialResults = new int[SPLITS];

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
		// init
		for (int i=0; i<N; i++) data[i] = i+1; // 1, 2, 3, 4, ..., N

		final Thread[] threads = new Thread[SPLITS];

		for (int i=0; i<SPLITS; i++) {
			threads[i] = new Thread(new Adder(i));
		}

		// MAP
		for (int i=0; i<SPLITS; i++) threads[i].start();

		barrier.await(); // wait for all threads

		// REDUCE
		int sum = 0;
		for (int i=0; i<SPLITS; i++) {
			sum += partialResults[i];
		}

		// should be (1 + N)*(N/2) = 500500
		System.out.println("Sum is "+sum);
	}
}
