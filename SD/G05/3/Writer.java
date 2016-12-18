public class Writer extends Thread {
	private final RWLock lock;
	private final int[] array;

	Writer(RWLock lock, int[] array) {
		this.lock = lock;
		this.array = array;
	}

	public void run() {
		try {
			lock.writeLock();
			for(int i = 0; i < array.length; i++)
				array[i] += 1;
			System.out.println("Done writing: " + array[0]);
			lock.writeUnlock();
		} catch (InterruptedException e) {
			System.out.println("Oops!");
		}
	}
}
