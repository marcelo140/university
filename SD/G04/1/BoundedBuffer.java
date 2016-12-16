public class BoundedBuffer {
	private int[] buffer;
	private int start, size;

	BoundedBuffer(int capacity) {
		buffer = new int[capacity];
		start = 0;
		size = 0;
	}

	synchronized public void put(int item) throws InterruptedException {
		// Bloquear enquanto o buffer estiver cheio
		while(full())
			wait();

		buffer[(start + size) % buffer.length] = item;
		size += 1;

		// As threads que estavam bloqueadas por o buffer estar vazio
		// já podem acordar. As restantes vão voltar a bloquear.
		notifyAll();
	}

	synchronized public int get() throws InterruptedException {
		int item;

		while(empty())
			wait();

		item = buffer[start];
		start = (start + 1) % buffer.length;
		size -= 1;

		// As threads que estavam bloqueadas por o buffer estar cheio
		// já podem acordar. As restantes vão voltar a bloquear.
		notifyAll();
		return item;
	}

	synchronized public boolean full() {
		return size == buffer.length;
	}

	synchronized public boolean empty() {
		return size == 0;
	}
}
