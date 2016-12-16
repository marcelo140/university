import java.util.concurrent.locks.*;
import java.util.Arrays;

public class Conta {
	private final int id;
	private float saldo;
	private Lock lock;

	Conta(int id, float saldo) {
		this.id = id;
		this.saldo = saldo;
		this.lock = new ReentrantLock();
	}

	public int getID() {
		return id;
	}

	public float consulta() {
		float r;

		// Impedir que ocorram transações
		// enquante se obtém o saldo
		lock.lock();
		r = saldo;
		lock.unlock();

		return r;
	}

	// Impedir que ocorram outras transações
	public void credito(float amount) {
		lock.lock();
		saldo += amount;
		lock.unlock();
	}

	// Impedir que ocorram outras transações
	public void debito(float amount) throws NotEnoughFunds {
		lock.lock();

		try {
			if (amount > saldo)
				throw new NotEnoughFunds("Saldo insuficiente");

			saldo -= amount;
		} finally {
			lock.unlock();
		}
	}

	// Bloquear array de contas ordenadamente de forma
	// a evitar deadlocks
	public static void lockAccounts(Conta[] contas) {
		Arrays.stream(contas)
			  .sorted((acc1, acc2) -> acc2.id - acc1.id)
			  .forEach(acc -> acc.lock.lock());
	}

	// Desbloquear contas não requer ordenação
	public static void unlockAccounts(Conta[] contas) {
		Arrays.stream(contas)
			  .forEach(acc -> acc.lock.unlock());
	}
}
