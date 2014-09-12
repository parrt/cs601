public class SynchronizeOnObject {
	int i = 3;
	Object gate = new Object();

	public void foo() {
		synchronized (gate) {
			i = i + 4;
		}
	}
	public void fee() {
		synchronized (gate) {
			i = i + 10;
		}
	}
}
