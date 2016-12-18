public class Consumer extends Thread {
	private Warehouse wh;

	Consumer(Warehouse wh) {
		this.wh = wh;
	}

	public void run() {
		try {
		wh.consume(new String[] {"1"});
		System.out.println("Got the cheese");
		wh.consume(new String[] {"2"});
		System.out.println("Got the milk");
		wh.consume(new String[] {"3"});
		System.out.println("Got the chocolate");
		wh.consume(new String[] {"1", "2", "3"});
		System.out.println("Got the dark cow");
		} catch (InterruptedException e) {
			System.out.println("Oops!");
		}
	}
}
