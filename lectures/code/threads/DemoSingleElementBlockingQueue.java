public class DemoSingleElementBlockingQueue {
	static SingleElementBlockingQueue q;

	static class Producer implements Runnable {
		public void run() {
			q.write("hello");
		}
	}

	static class Consumer implements Runnable {
		public void run() {
			System.out.println("data is "+q.remove());
		}
	}

	public static void main(String[] args) throws Exception {
		q = new SingleElementBlockingQueue();
		new Thread(new Consumer()).start();
		Thread.sleep(2000);
		new Thread(new Producer()).start();
	}
}
