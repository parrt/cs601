import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** Use a lock to protect and increment operation and a barrier
 *  to wait for all operations to finish.
 */
public class DemoLock {
	public static final int N = 10000;
	private static int count = 0;
	private static Lock lock = new ReentrantLock();
	private static CyclicBarrier barrier = new CyclicBarrier(3);

	static class Operation implements Runnable {
		public void run() {
			for (int i=1; i<=N; i++) {
				increment();
//				unsafeIncrement();
			}
			try {barrier.await();}
			catch (Exception e) {e.printStackTrace();}
		}

		void increment() {
			lock.lock();
			try {
				count = count + 1;
			}
			finally {
				lock.unlock();
			}
		}

		/** results in output like "Count 11148 should be 20000" */
		void unsafeIncrement() {
			count = count + 1;
		}
	}

	public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
		Operation op = new Operation();
		Thread t = new Thread(op);
		t.start();

		Operation op2 = new Operation();
		Thread t2 = new Thread(op2);
		t2.start();

		barrier.await();
		System.out.printf("Count %d should be %d\n", count, N*2);
	}
}
