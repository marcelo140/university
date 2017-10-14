public class Cliente extends Thread {
	private final Banco banco;

	Cliente(Banco banco) {
		this.banco = banco;
	}

	public void run() {
		banco.credito(1, 100);
		banco.credito(2, 100);
		banco.transferir(1, 2, 50);
		banco.transferir(2, 1, 25);
		banco.transferir(1, 2, 40);
		banco.transferir(1, 2, 5);
		banco.transferir(1, 2, 10);
		banco.debito(2, 100);
		// conta1 += 20
		// conta2 += 80
	}
}
