public class Cliente extends Thread {
	private final Banco banco;

	Cliente(Banco banco) {
		this.banco = banco;
	}

	public void run() {
		banco.transferir(1, 2, 50);
		banco.transferir(1, 2, 50);
		banco.transferir(2, 1, 25);
		banco.transferir(1, 2, 10);
		banco.transferir(2, 1, 25);
		banco.transferir(1, 2, 40);
		banco.transferir(1, 2, 10);
		banco.transferir(1, 2, 50);
		banco.transferir(2, 1, 25);
		banco.transferir(1, 2, 40);
		banco.transferir(1, 2, 10);
		banco.transferir(2, 1, 25);
		banco.transferir(1, 2, 10);
	}
}
