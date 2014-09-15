class ParallelComputation implements Runnable {
	Barrier barrier;

	ParallelComputation(Barrier barrier) {
		this.barrier = barrier;
	}

	public void run() {
        // DO SOME COMPUTATION
		// now wait for others to finish
        try {
			barrier.wakeup();
            barrier.waitForRelease();
			System.out.println("thread released when count="+barrier.count);
		}
        catch(InterruptedException e) { e.printStackTrace(); }
    }
}
