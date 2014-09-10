import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// demonstrate concurrent map access; particularly putIfAbsent
public class ConcMap {
	static ConcurrentMap<String, String> map =
		new ConcurrentHashMap<String, String>();

	static class Putter implements Runnable {
		public void run() {
			String prev = map.get("parrt");
			if ( prev==null ) {
				try { Thread.sleep((long)Math.random()*1000); }
				catch (InterruptedException ie) { }
				prev = map.putIfAbsent("parrt", "x5707");
				if ( prev==null ) {
					System.out.println("added in "+
									   Thread.currentThread().getName());
				}
				else {
					System.out.println("prev value "+prev+"in "+
									   Thread.currentThread().getName());
				}
			}
			else {
				String phone = map.get("parrt");
				System.out.println("got value "+phone+"in "+
								   Thread.currentThread().getName());
			}
		}
	}

	public static void main(String[] args) {
		final int N = 10;
		final Thread[] threads = new Thread[N];

		for (int i=0; i<N; i++) {
			threads[i] = new Thread(new Putter());
		}

		for (int i=0; i<N; i++) threads[i].start();
	}
}
