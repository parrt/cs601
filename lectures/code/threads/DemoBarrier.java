public class DemoBarrier {
	public static final int N = 100;
	public static void main(String[] args) {
		Barrier barrier = new Barrier(N);
		for (int i=1; i<=N; i++) {
			new Thread(new ParallelComputation(barrier)).start();
		}
	}
}
