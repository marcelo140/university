public class Tester {
	private static final int nClients = 100;

	public static void main(String[] args) throws InterruptedException, InvalidAccount {
		Banco banco = new Banco();
		Thread[] threads = new Thread[nClients];

		banco.createAccount(1000);
		banco.createAccount(1000);

		for(int i = 0; i < nClients; i++) {
			threads[i] = new Cliente(banco);
			threads[i].start();
		}

		for(int i = 0; i < nClients; i++)
			threads[i].join();

		// Saldo total das duas primeiras contas deve ser 2000
		float saldo = banco.totalBalance(new int[] {0, 1});
		System.out.println(saldo);
	}
}
