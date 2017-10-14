public class Cliente extends Thread {
	private final Banco banco;

	Cliente(Banco banco) {
		this.banco = banco;
	}

	public void run() {
		int conta = banco.createAccount(500);
		float saldo;

		try {
			banco.transfer(conta, 1, 250);
			banco.transfer(conta, 0, 150);
			banco.transfer(0, conta, 150);
			banco.transfer(1, conta, 250);
			saldo = banco.totalBalance(new int[] {0, 1, conta});
			System.out.println(saldo);
			banco.closeAccount(conta);
		} catch (InvalidAccount | NotEnoughFunds e) {
			System.out.println(e.getMessage() + conta);
		}
	}
}
