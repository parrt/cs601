// Demo simple ThreadLocal
public class TransactionIDs {
	static final ThreadLocal<Integer> ID =
		new ThreadLocal<Integer>() {
			protected Integer initialValue() { return 0; }
		};

	public static int getID() {
		int id = ID.get() + 1;
		ID.set(id);
		return id;
	}

	static class BusinessLogic implements Runnable {
		public void run() {
			while (true) {
				System.out.println(Thread.currentThread().getName()+": "+getID());
				try {Thread.sleep(800);} catch (InterruptedException ie) {
					System.err.println("leave me alone!");
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		final int N = 10;
		final Thread[] threads = new Thread[N];

		for (int i=0; i<N; i++) {
			threads[i] = new Thread(new BusinessLogic());
		}

		for (int i=0; i<N; i++) threads[i].start();
	}
}
