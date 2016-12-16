public class Counter {
	int num;

	Counter() {
		num = 0;
	}

	public void increment() {
		num += 1;
	}

	public String toString() {
		return Integer.toString(num);
	}
}
