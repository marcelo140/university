public class Tester {
	private static final int nClients = 100;
	private static final int nContas = 3;

	public static void main(String[] args) throws InterruptedException {
		Banco banco = new Banco(nContas);
		Thread[] threads = new Thread[nClients];

		banco.credito(1, 1000);
		banco.credito(2, 1000);

		for(int i = 0; i < nClients; i++) {
			threads[i] = new Cliente(banco);
			threads[i].start();
		}

		for(int i = 0; i < nClients; i++)
			threads[i].join();

		System.out.println("Conta1:" + banco.consulta(1));
		System.out.println("Conta2:" + banco.consulta(2));
	}
}
