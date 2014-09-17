import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoConcurrentAtomInt {
	final static int N = 10;
	static AtomicInteger n = new AtomicInteger(0);
	static final CyclicBarrier barrier = new CyclicBarrier(N+1);

	static class Depositor implements Runnable {
		int v;
		public Depositor(int v) {
			this.v = v;
		}
		public void run() {
			while ( true ) {
				int current = n.get();
				int newValue = current + v;
				if ( n.compareAndSet(current, newValue) ) {
					break;
				}
			}
			try {barrier.await();}
			catch (Exception e) { e.printStackTrace(); }
		}
	}

	public static void main(String[] args) {
		final Thread[] threads = new Thread[N];

		for (int i=0; i<N; i++) {
			threads[i] = new Thread(new Depositor((i+1)*10));
		}

		// 10+20+30+...10*10 = 550 if N=10
		for (int i=0; i<N; i++) {
			threads[i].start();
		}

		try {barrier.await();}
		catch (Exception e) { }

		System.out.println(n.get());
	}
}
