import java.util.concurrent.Callable;

class ProducerActor implements Callable<Void> {
	ConsumerActor consumer;
	ProducerActor(ConsumerActor consumer) {
		this.consumer = consumer;
	}
	public Void call() {
		for (int i = 1; i<= DemoActor.N; i++) {
			consumer.enqueue(i);
		}
		consumer.enqueue(DemoActor.EOF);
		return null; // just to satisfy the Java compiler's Void type
	}
}
