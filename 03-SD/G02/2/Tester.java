public class Tester {
	private static final int nClients = 100;

	public static void main(String[] args) throws InterruptedException {
		int num = Integer.parseInt(args[0]);
		Banco banco = new Banco(num);
		Thread[] threads = new Thread[nClients];

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
