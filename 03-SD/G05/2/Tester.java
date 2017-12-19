public class Tester {
	private static final int nThreads = 100;

	public static void main(String[] args) throws InterruptedException {
		Warehouse wh = new Warehouse();
		Thread[] producers = new Thread[nThreads];
		Thread[] consumers = new Thread[nThreads];

		for(int i = 0; i < nThreads; i++) {
			producers[i] = new Producer(wh);
			consumers[i] = new Consumer(wh);
			producers[i].start();
			consumers[i].start();
		}

		for(int i = 0; i < nThreads; i++) {
			producers[i].join();
			consumers[i].join();
		}

		for(int i = 1; i <= 5; i++)
			System.out.println(wh.getStock("" + i));
	}
}
