import java.util.*;

// A sincronização agora é feita nas próprias contas,
// permitindo que várias threads acedam ao banco em
// simultâneo desde que seja a contas diferentes

public class Banco {
	private final int nContas;
	private List<Conta> contas;

	Banco(int nContas) {
		this.nContas = nContas;
		contas = new ArrayList<>(nContas);

		for(int i = 0; i < nContas; i++)
			contas.add(new Conta());
	}

	public float consulta(int conta) {
		return contas.get(conta).consulta();
	}

	public void credito(int conta, float montante) {
		contas.get(conta).credito(montante);
	}

	public void debito(int conta, float montante) {
		contas.get(conta).debito(montante);
	}

	public void transferir(int from, int to, float montante) {
		Conta c1 = contas.get(from);
		Conta c2 = contas.get(to);

		// Os objetos devem ser bloqueados sempre pela mesmo ordem
		// de forma a evitar deadlocks.
		if (to < from){
			synchronized (c1) {
				synchronized (c2) {
					c1.debito(montante);
					c2.credito(montante);
				}
			}
		} else {
			synchronized (c2) {
				synchronized (c1) {
					c1.debito(montante);
					c2.credito(montante);
				}
			}
		}

	}
}
