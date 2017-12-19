public class Reader extends Thread {
	private final RWLock lock;
	private final int[] array;

	Reader(RWLock lock, int[] array) {
		this.lock = lock;
		this.array = array;
	}

	public void run() {
		boolean equal = true;

		try {
			lock.readLock();
			for(int i = 1; equal && i < array.length; i++)
				equal = equal && array[i] == array[i-1];
			System.out.println("All elements equal to " + array[0] + "? " + equal);
			lock.readUnlock();
		} catch (InterruptedException e) {
			System.out.println("Oops!");
		}
	}
}
