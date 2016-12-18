public class Tester {
	private static final int nConsumers = 50;
	private static final int nProducers = 50;
	private static final int capacity = 100;

	public static void main(String[] args) throws InterruptedException {
		BoundedBuffer buffer = new BoundedBuffer(capacity);
		Thread[] consumers = new Thread[nConsumers];
		Thread[] producers = new Thread[nProducers];

		for(int i = 0; i < nConsumers; i++) {
			consumers[i] = new Consumer(buffer);
			consumers[i].start();
		}

		for(int i = 0; i < nProducers; i++) {
			producers[i] = new Producer(buffer);
			producers[i].start();
		}
	}
}
