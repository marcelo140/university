public class Tester {
	public static void main(String[] args) {
		int nThreads = Integer.parseInt(args[0]);
		int max = Integer.parseInt(args[1]);
		Thread[] threads = new Thread[nThreads];

		for(Thread t: threads) {
			t = new Printer(max);
			t.start();
		}
	}
}
