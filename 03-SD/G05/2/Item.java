public class Item {
	private int quantity;

	Item() {
		quantity = 0;
	}

	synchronized public void supply(int quantity) {
		this.quantity += quantity;
		notifyAll();
	}

	synchronized public void consume() {
		quantity -= 1;
	}

	synchronized public int stock() {
		return quantity;
	}

	synchronized public boolean waitForStock() throws InterruptedException {
		boolean waited = false;

		if (quantity == 0) {
			waited = true;
			wait();
		}

		return waited;
	}
}
