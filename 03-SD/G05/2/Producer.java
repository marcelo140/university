public class Producer extends Thread {
	private Warehouse wh;

	Producer(Warehouse wh) {
		this.wh = wh;
	}

	public void run() {
		wh.supply("1", 2);
		wh.supply("2", 2);
		wh.supply("3", 2);
	}
}
