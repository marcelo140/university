public class Printer extends Thread {
	private Barreira barreira;

	Printer(Barreira barreira) {
		this.barreira = barreira;
	}

	public void run() {
		try {
			for(int i = 0; i < 5; i++){
				System.out.println(i);
				barreira.espera();
			}
		} catch(InterruptedException e) {
			System.out.println("Oops!");
		}
	}
}
