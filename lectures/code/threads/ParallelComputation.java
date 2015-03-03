class ParallelComputation implements Runnable {
	Barrier barrier;

	ParallelComputation(Barrier barrier) {
		this.barrier = barrier;
	}

	public void run() {
        // DO SOME COMPUTATION
		// now wait for others to finish
        try {
			// barrier.wakeup(); an action such as this is why we nee a loop in Barrier.waitForRelease()
            barrier.waitForRelease();
			System.out.println("thread released when count="+barrier.count);
		}
        catch(InterruptedException e) { e.printStackTrace(); }
    }
}
