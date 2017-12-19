import java.util.*;
import java.util.concurrent.locks.*;
import java.util.Arrays;

public class Warehouse {
	private Map<String, Item> items;
	Lock lock;

	Warehouse() {
		items = new TreeMap<>();
		lock = new ReentrantLock();
	}

	public void supply(String item, int quantity) {
		Item it = getItem(item);
		it.supply(quantity);
	}

	public void consume(String[] items) throws InterruptedException {
		Item[] itemList = new Item[items.length];

		for(int i = 0; i < items.length; i++)
			itemList[i] = getItem(items[i]);

		// O lock garante que após verificar que os recursos estão disponíveis nenhuma
		// outra thread vai consumir esses mesmos recursos
		lock.lock();
		waitForStock(itemList);
		Arrays.stream(itemList).forEach(it -> it.consume());
		lock.unlock();
	}

	public int getStock(String item) {
		Item it = getItem(item);
		return it.stock();
	}

	// Este método é o único que acede diretamente ao Map de items, daí ser o único que
	// requer sincronização
	synchronized private Item getItem(String id) {
		Item it = items.get(id);

		if (it == null) {
			it = new Item();
			items.put(id, it);
		}

		return it;
	}

	private void waitForStock(Item[] items) throws InterruptedException {
		int i = 0;
		while(i < items.length)
			i = items[i].waitForStock() ? 0 : i+1;
	}
}
