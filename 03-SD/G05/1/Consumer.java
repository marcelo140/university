public class Consumer extends Thread {
	private final BoundedBuffer buffer;

	Consumer(BoundedBuffer buffer) {
		this.buffer = buffer;
	}

	public void run() {
		int item;
		try {
			for(int i = 0; i < 10; i++) {
				item = buffer.get();
				System.out.println(item);
			}
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}
