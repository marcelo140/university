// O step permite reutilizar a barreira. As threads, à medida que chegam, bloqueieam
// no step 0. A última thread acorda as restantes e muda a barreira para step 1,
// permitindo que as threads continuem sem bloquear ao acordar (porque a barreira
// está numa step diferente), e que as threads que acordaram bloqueim novamente.

public class Barreira {
	private final int maxThreads;
	private int counter, step;

	Barreira(int maxThreads) {
		this.maxThreads = maxThreads;
		counter = 0;
		step = 0;
	}

	synchronized public void espera() throws InterruptedException {
		int my_step = step;
		counter += 1;

		// Se for a última thread acordar as que estão bloqueadas
		if (counter == maxThreads) {
	 		notifyAll();
			counter = 0;
			// As threads que estão bloqueadas não acordam imediatamente. Como o
			// counter vai estar a 0 quando elas acordarem, usamos o step para permitir
			// que elas não bloqueiem novamente, visto que a barreira já vai estar
			// numa fase (step) diferente.
			step = 1 - step;
		} else {
			while(counter < maxThreads && my_step == step)
				wait();
		}
	}
}
