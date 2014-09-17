import java.util.concurrent.locks.LockSupport;

/** Demo a monitoring thread and wakes up at regular intervals
 *  to figure out whether a producer and consumer are blocked, waiting,
 *  or runnable (it ignores the sleeping condition). And then print statistics
 *  after running for 5 seconds.
 */
public class DemoThreadMonitoring {
	public static final long MONITORING_PERIOD = 1000L;
	static SingleElementBlockingQueue q;
	static Thread producer, consumer;

	static volatile boolean done = false;

	static class Producer implements Runnable {
		public void run() {
			while (!done) q.write("hello");
		}
	}

	static class Consumer implements Runnable {
		public void run() {
			while (!done) q.remove();
		}
	}

	static class Monitor implements Runnable {
		public int numEvents = 0;
		public int producerBlocked = 0;
		public int producerWaiting = 0;
		public int consumerBlocked = 0;
		public int consumerWaiting = 0;
		@Override
		public void run() {
			while (!done) {
				numEvents++;
				if ( producer.getState() == Thread.State.BLOCKED ) producerBlocked++;
				if ( producer.getState() == Thread.State.WAITING ) producerWaiting++;
				if ( consumer.getState() == Thread.State.BLOCKED ) consumerBlocked++;
				if ( consumer.getState() == Thread.State.WAITING ) consumerWaiting++;

				LockSupport.parkNanos(MONITORING_PERIOD);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		q = new SingleElementBlockingQueue();
		consumer = new Thread(new Consumer());
		consumer.start();
		producer = new Thread(new Producer());
		producer.start();

		Monitor monitor = new Monitor();
		Thread monitorThread = new Thread(monitor);
		monitorThread.start();

		Thread.sleep(5000);
		done = true;

		System.out.printf("Producer: (%d blocked + %d waiting) / %d samples = %1.2f%% wasted\n",
						  monitor.producerBlocked,
						  monitor.producerWaiting,
						  monitor.numEvents,
						  100.0*(monitor.producerBlocked+monitor.producerWaiting)/monitor.numEvents);
		System.out.printf("Consumer: (%d blocked + %d waiting) / %d samples = %1.2f%% wasted\n",
						  monitor.consumerBlocked,
						  monitor.consumerWaiting,
						  monitor.numEvents,
						  100.0*(monitor.consumerBlocked+monitor.consumerWaiting)/monitor.numEvents);
	}
}
