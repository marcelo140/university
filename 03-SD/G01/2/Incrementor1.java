public class Incrementor1 extends Thread {
	private final Counter counter;
	private int max;

	Incrementor1(Counter counter, int num) {
		this.counter = counter;
		max = num;
	}

	public void run() {
		for(int i = 0; i < max; i++)
			counter.increment();
	}
}
