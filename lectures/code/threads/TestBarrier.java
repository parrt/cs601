public class TestBarrier {
	public static void main(String[] args) {
		Barrier barrier = new Barrier(3);
		new Thread(new ParallelComputation(barrier)).start();
		new Thread(new ParallelComputation(barrier)).start();
		// if you comment this one out, program hangs!
		new Thread(new ParallelComputation(barrier)).start();
	}
}
