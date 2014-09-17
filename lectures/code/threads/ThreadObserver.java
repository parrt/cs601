import java.util.concurrent.locks.LockSupport;

/** A runnable class that attaches to another thread and wakes up
 *  at regular intervals to determine that thread's state. The goal
 *  is to figure out how much time that thread is blocked, waiting,
 *  or sleeping.
 */
class ThreadObserver implements Runnable {
	public static final long MONITORING_PERIOD = 1000L;

	protected int numEvents = 0;
	protected int blocked = 0;
	protected int waiting = 0;
	protected int sleeping = 0;

	protected boolean done = false;

	protected final Thread threadToMonitor;

	public ThreadObserver(Thread threadToMonitor) {
		this.threadToMonitor = threadToMonitor;
	}

	@Override
	public void run() {
		while (!done) {
			numEvents++;
			switch ( threadToMonitor.getState() ) {
				case BLOCKED: blocked++; break;
				case WAITING: waiting++; break;
				case TIMED_WAITING: sleeping++; break;
			}
			LockSupport.parkNanos(MONITORING_PERIOD);
		}
	}

	public void terminate() { done = true; }

	public String toString() {
		return String.format("(%d blocked + %d waiting + %d sleeping) / %d samples = %1.2f%% wasted",
							 blocked,
							 waiting,
							 sleeping,
							 numEvents,
							 100.0*(blocked + waiting + sleeping)/numEvents);
	}
}
