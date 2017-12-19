public class Conta {
	private float saldo;

	Conta() {
		saldo = 0;
	}

	public float consulta() {
		return saldo;
	}

	public void credito(float montante) {
		saldo += montante;
	}

	public void debito(float montante) {
		saldo -= montante;
	}
}
