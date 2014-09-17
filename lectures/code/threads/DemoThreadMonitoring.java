import java.util.concurrent.locks.LockSupport;

public class DemoThreadMonitoring {
	public static final long nanos = 1000L;
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
		public int consumerBlocked = 0;
		@Override
		public void run() {
			while (!done) {
				numEvents++;
				if ( producer.getState() == Thread.State.BLOCKED ) producerBlocked++;
				if ( consumer.getState() == Thread.State.BLOCKED ) consumerBlocked++;
				LockSupport.parkNanos(nanos);
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

		System.out.printf("Producer: %d/%d runnable/blocked = %1.4f%%\n",
						  monitor.producerBlocked,
						  monitor.numEvents,
						  ((float)monitor.producerBlocked*100)/monitor.numEvents);
		System.out.printf("Consumer: %d/%d runnable/blocked = %1.4f%%\n",
						  monitor.consumerBlocked,
						  monitor.numEvents,
						  ((float)monitor.consumerBlocked*100)/monitor.numEvents);
	}
}
