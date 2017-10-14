public class Tester {
	private static final int nThreads = 100;

	public static void main(String[] args) {
		Barreira barreira = new Barreira(nThreads);
		Thread[] threads = new Thread[nThreads];

		for(int i = 0; i < nThreads; i++) {
			threads[i] = new Printer(barreira);
			threads[i].start();
		}
	}
}
