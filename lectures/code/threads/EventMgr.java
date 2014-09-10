import java.util.ArrayList;
import java.util.List;

// Demo ThreadLocal holding list of strings per thread
public class EventMgr {
	public static final ThreadLocal<List<String>> eventsPerThread =
		new ThreadLocal<List<String>>() {
			protected List<String> initialValue() {
				return new ArrayList<String>();
			}
		};

	public static void log(String event) {
		eventsPerThread.get().add(event);
	}

	public static List<String> getLogs() {
		return eventsPerThread.get();
	}

	public static void main(String[] args) throws Exception {
		final int N = 100;
		final Thread[] threads = new Thread[N];

		for (int i=0; i<N; i++) {
			threads[i] = new Thread() {
				public void run() {
					EventMgr.log(Thread.currentThread().getName()+": hello");
				}
			};
		}

		for (int i=0; i<N; i++) threads[i].start();

		// thread locals disappear after threads exit
	}
}
