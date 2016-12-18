public class Tester {
	private static final int nThread = 100;

	public static void main(String[] args) throws InterruptedException {
		RWLock lock = new RWLock(5);
		int[] array = new int[10000];
		Thread[] readers = new Thread[nThread];
		Thread[] writers = new Thread[nThread];

		for(int i = 0; i < nThread; i++) {
			readers[i] = new Reader(lock, array);
			writers[i] = new Writer(lock, array);
			readers[i].start();
			writers[i].start();
		}

		for(int i = 0; i < nThread; i++) {
			readers[i].join();
			writers[i].join();
		}
	}
}
