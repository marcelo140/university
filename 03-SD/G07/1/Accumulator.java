public class Accumulator {
	private float sum;

	Accumulator() {
		sum = 0;
	}

	synchronized public float increment(float num) {
		sum += num;
		return sum;
	}
}
