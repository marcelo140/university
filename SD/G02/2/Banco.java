import java.util.*;

// Utlizar synchronized para impedir que mais que 1 thread
// aceda ao banco em simultâneo

public class Banco {
	private final int nContas;
	private List<Conta> contas;

	Banco(int nContas) {
		this.nContas = nContas;
		contas = new ArrayList<>(nContas);

		for(int i = 0; i < nContas; i++)
			contas.add(new Conta());
	}

	public synchronized float consulta(int conta) {
		return contas.get(conta).consulta();
	}

	public synchronized void credito(int conta, float montante) {
		contas.get(conta).credito(montante);
	}

	public synchronized void debito(int conta, float montante) {
		contas.get(conta).debito(montante);
	}

	public synchronized void transferir(int conta1, int conta2, float montante) {
		contas.get(conta1).debito(montante);
		contas.get(conta2).credito(montante);
	}
}
