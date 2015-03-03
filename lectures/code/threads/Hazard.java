import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Hazard {
	static class Producer implements Runnable {
		String str;
		public Producer(String s) { str = s; }
		public void run() {
			int i = 0;
			while ( i<5 ) {
				if ( !data.contains(str) ) {
					Thread.yield();
					data.add(str);
				}
				i++;
			}
		}
	}

	static class Consumer implements Runnable {
		public void run() {
			int i = 0;
			while ( i<5 ) {
				System.out.println(data);
				i++;
			}
		}
	}

	public static List<String> data = new ArrayList<String>();

	public static void main(String[] args) {
		Producer p = new Producer("X");
		Producer p2 = new Producer("X");
		Consumer c = new Consumer();
		new Thread(p).start();
		new Thread(p2).start();
		Thread u = new Thread(c);
		u.start();
	}
}
