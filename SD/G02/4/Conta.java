public class Conta {
	private float saldo;

	Conta() {
		saldo = 0;
	}

	public synchronized float consulta() {
		return saldo;
	}

	public synchronized void credito(float montante) {
		saldo += montante;
	}

	public synchronized void debito(float montante) {
		saldo -= montante;
	}
}
