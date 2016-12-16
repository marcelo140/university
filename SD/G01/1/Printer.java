public class Printer extends Thread {
	private final int max;

	Printer(int n) {
		max = n;
	}

	public void run() {
		for(int i = 1; i <= max; i++)
			System.out.println(i);
	}
}
