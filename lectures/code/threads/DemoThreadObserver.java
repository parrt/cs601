/** Demo a monitoring thread and wakes up at regular intervals
 *  to figure out whether a producer and consumer are blocked (enter/exit
 *  a synchronized method), waiting,
 *  or runnable (it ignores the sleeping condition). And then print statistics
 *  after running for 5 seconds.
 */
public class DemoThreadObserver {
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

	public static void main(String[] args) throws Exception {
		q = new SingleElementBlockingQueue();
		consumer = new Thread(new Consumer());
		consumer.start();
		producer = new Thread(new Producer());
		producer.start();

		ThreadObserver producerObserver = new ThreadObserver(producer);
		new Thread(producerObserver).start();
		ThreadObserver consumerObserver = new ThreadObserver(consumer);
		new Thread(consumerObserver).start();

		Thread.sleep(5000);
		producerObserver.terminate();
		consumerObserver.terminate();
		done = true;

		System.out.println("Producer: "+producerObserver);
		System.out.println("Consumer: "+consumerObserver);
	}
}
