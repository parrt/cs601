import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

class ConsumerActor implements Callable<Void> {
//		static Vector<Object> queue = new Vector<Object>(MAX_BUFFER_SIZE);
	static List<Object> queue = new ArrayList<Object>(DemoActor.MAX_BUFFER_SIZE);

	@Override
	public Void call() throws Exception {
		Object o = take();
		while ( o != DemoActor.EOF ){
			o = take();
		}
		return null;
	}
	public synchronized Object take() {
		while ( queue.size()==0 ) {
			try { wait(); }
			catch (InterruptedException ie) {
				System.err.println("interruped?");
			}
		}
		Object o = queue.remove(0);
		notify();
		return o;
	}
	public synchronized void enqueue(Object o) {
		while ( queue.size() >= DemoActor.MAX_BUFFER_SIZE ) {
			try { wait(); }
			catch (InterruptedException ie) {
				System.err.println("interruped?");
			}
		}
		queue.add(o);
		notify();
	}
}
