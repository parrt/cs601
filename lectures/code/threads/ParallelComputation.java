class ParallelComputation implements Runnable {
	Barrier barrier;

	ParallelComputation(Barrier barrier) {
		this.barrier = barrier;
	}

	public void run() {
        // DO SOME COMPUTATION
		// now wait for others to finish
        try {
            barrier.waitForRelease();
        }
        catch(InterruptedException e) { e.printStackTrace(); }
    }
}
