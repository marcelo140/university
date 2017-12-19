import java.util.concurrent.locks.*;

public class RWLock {
	private final int maxReaders;                // Número máximo de leitores em simultâneo para evitar starvation dos escritores
	private int readers = 0;                     // Número de leitores que entraram na zona crítica desde a última escrita
	private int writers_waiting = 0;			 // Número de escritores à espera
	private int reading = 0;                     // Número de leitores atualmente na zona crítica
	private boolean writing = false;
	private Lock lock = new ReentrantLock();
	private Condition notWriting = lock.newCondition();
	private Condition notReading = lock.newCondition();

	RWLock(int maxReaders) {
		this.maxReaders = maxReaders;
	}

	public void readLock() throws InterruptedException {
		lock.lock();
		// Aguarda se houver um escritor ou o número de leitores tiver chegado ao limite
		while (writing == true || readers == maxReaders)
			notWriting.await();

		reading += 1;
		readers += 1;

		lock.unlock();
	}

	public void readUnlock() {
		lock.lock();

		reading -= 1;

		// Se houver escritores à espera dar-lhes oportunidade de forma a evitar starvation
		if (reading == 0 && writers_waiting > 0)
			notReading.signalAll();
		else if (reading == 0) {
			readers = 0;
			notWriting.signalAll();
		}

		lock.unlock();
	}

	public void writeLock() throws InterruptedException {
		lock.lock();
		while (writing == true || reading > 0) {
			writers_waiting += 1;
			notReading.await();
		}

		writers_waiting -= 1;
		writing = true;

		lock.unlock();
	}

	// Permitir acesso a todos os leitores e escritores
	public void writeUnlock() {
		lock.lock();
		readers = 0;
		writing = false;
		notWriting.signalAll();
		notReading.signalAll();
		lock.unlock();
	}
}
