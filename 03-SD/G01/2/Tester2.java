public class Tester2 {
	public static void main(String[] args) throws InterruptedException {
		int nThreads = Integer.parseInt(args[0]);
		int max = Integer.parseInt(args[1]);
		Thread[] threads = new Thread[nThreads];
		Counter counter = new Counter();

		for(int i = 0; i < nThreads; i++) {
			threads[i] = new Incrementor2(counter, max);
			threads[i].start();
		}

		// Espera que todas as threads terminem
		for(int i = 0; i < nThreads; i++)
			threads[i].join();

		System.out.println(counter);
	}
}

