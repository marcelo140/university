public class Counter {
	private int num;

	Counter() {
		num = 0;
	}

	// Utilizar synchronized para evitar corridas
	synchronized public void increment() {
		num += 1;
	}

	public String toString() {
		return Integer.toString(num);
	}
}
