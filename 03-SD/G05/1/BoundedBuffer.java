import java.util.concurrent.locks.*;

public class BoundedBuffer {
	private int[] buffer;
	private int start, size;
	Lock lock;
	Condition empty, full;

	BoundedBuffer(int capacity) {
		buffer = new int[capacity];
		start = 0;
		size = 0;
		lock = new ReentrantLock();
		empty = lock.newCondition();
		full = lock.newCondition();
	}

	public void put(int item) throws InterruptedException {
		lock.lock();
		// Bloquear thread por não haver espaço no buffer
		while(size == buffer.length)
			full.await();

		buffer[(start + size) % buffer.length] = item;
		size += 1;

		// As threads que estavam bloqueadas por o buffer estar vazio
		// já podem acordar.
		empty.signalAll();
		lock.unlock();
	}

	public int get() throws InterruptedException {
		int item;

		lock.lock();
		// Bloquear thread por o buffer estar vazio
		while(size == 0)
			empty.await();

		item = buffer[start];
		start = (start + 1) % buffer.length;
		size -= 1;

		// As threads que estavam bloqueadas por o buffer estar cheio
		// já podem acordar.
		full.signalAll();
		lock.unlock();

		return item;
	}
}
