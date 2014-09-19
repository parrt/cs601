public class DemoSingleElementBlockingQueue {
	static SingleElementBlockingQueue q;

	static class Producer implements Runnable {
		public void run() {
			q.write("hello");
			q.write("again");
		}
	}

	static class Consumer implements Runnable {
		public void run() {
			System.out.println("data is "+q.remove());
			System.out.println("data is "+q.remove());
		}
	}

	public static void main(String[] args) throws Exception {
		q = new SingleElementBlockingQueue();
		new Thread(new Consumer()).start();
		Thread.sleep(1000);
		new Thread(new Producer()).start();
	}
}
