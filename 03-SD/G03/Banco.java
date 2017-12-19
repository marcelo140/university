import java.util.*;
import java.util.concurrent.locks.*;
import java.util.stream.*;

public class Banco implements Bank {
	Lock lock;
	private Map<Integer, Conta> contas;
	private int nContas;

	Banco() {
		lock = new ReentrantLock();
		nContas = 0;
		contas = new HashMap<>();
	}

	// Impedir acesso ao banco enquanto se criar a conta
	public int createAccount(float initialBalance) {
		lock.lock();
		Conta conta = new Conta(nContas, initialBalance);

		contas.put(nContas, conta);
		nContas += 1;
		lock.unlock();

		return conta.getID();
	}

	// Impedir acesso ao banco enquanto se remove a conta
	public float closeAccount(int id) throws InvalidAccount {
		lock.lock();
		try {
			Conta c = getAccount(id);
			contas.remove(id);
			return c.consulta();
		} finally {
			lock.unlock();
		}
	}

	public float totalBalance(int accounts[]) {
		float balance = 0;
		Conta[] lista_contas = new Conta[accounts.length];

		// Impedir que sejam criadas/removidas contas enquanto se
		// obtém todas as contas pretendidas
		try {
			lock.lock();
			for(int i = 0; i < accounts.length; i++)
				lista_contas[i] = contas.get(accounts[i]);
		} finally {
			lock.unlock();
		}

		// Impedir que se efetuem transações enquanto recolhemos
		// o saldo de cada conta
		try {
			Conta.lockAccounts(lista_contas);
			for(Conta c: lista_contas)
				balance += c.consulta();

			return balance;
		} finally {
			Conta.unlockAccounts(lista_contas);
		}
	}

	public void transfer(int from, int to, float amount) throws InvalidAccount, NotEnoughFunds {
		Conta c1, c2;

		// Impedir acesso ao banco enquanto se obtém
		// as contas pretendidas
		try {
			lock.lock();
			c1 = getAccount(from);
			c2 = getAccount(to);
		} finally {
			lock.unlock();
		}

		transferHelper(c1, c2, amount);
	}

	private void transferHelper(Conta from, Conta to, float amount) throws NotEnoughFunds {
		Conta[] contas = new Conta[] {from, to};

		// Impedir que se efetuem transações enquanto recolhemos
		// o saldo de cada conta
		Conta.lockAccounts(contas);
		try {
			from.debito(amount);
			to.credito(amount);
		} finally {
			Conta.unlockAccounts(contas);
		}
	}

	private Conta getAccount(int id) throws InvalidAccount {
		// Impedir acesso ao banco enquanto se obtém
		// a conta pretendida
		lock.lock();
		Conta c = contas.get(id);
		lock.unlock();

		if (c == null)
			throw new InvalidAccount("Conta inexistente");

		return c;
	}

}
