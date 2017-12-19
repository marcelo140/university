import java.net.*;
import java.io.*;

public class Client extends Thread {
	Socket cli;
	BufferedReader in;
	PrintWriter out;
	Banco banco;

	Client(Socket cli, Banco banco) throws IOException {
		this.banco = banco;
		this.cli = cli;
		in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
		out = new PrintWriter(cli.getOutputStream());
	}

	public void run() {
		String linha = null;
		System.out.println(cli.getInetAddress() + " connected");

		while((linha = readLine()) != null) {
			String r = interpreteRequest(linha);

			out.println(r + "\n");
			out.flush();
		}

		System.out.println(cli.getInetAddress() + " disconnected");
	}

	private String interpreteRequest(String request) {
		String[] keywords = request.split(" ");
		String r;

		switch(keywords[0].toUpperCase()) {
			case "CREATE":
				r = "Success! Account id: " + createAccount(keywords);
				break;
			case "CLOSE":
				r = "Success! Amount in account: " + closeAccount(keywords);
				break;
			case "TRANSFER":
				r = transfer(keywords);
				break;
			case "BALANCE":
				r = "Total balance: " + totalBalance(keywords);
				break;
			default:
				r = "InvalidCommand";
				break;
		}

		return r;
	}

	private String createAccount(String[] keywords) {
		float amount = Float.parseFloat(keywords[1]);
		return "" + banco.createAccount(amount);
	}

	private String closeAccount(String[] keywords) {
		String r;

		try {
			int acc = Integer.parseInt(keywords[1]);
			r = "" + banco.closeAccount(acc);;
		} catch(InvalidAccount e) {
			r = "InvalidAccount";
		}

		return r;
	}

	private String totalBalance(String[] keywords) {
		int[] accounts = new int[keywords.length - 1];

		for(int i = 1; i < keywords.length; i++)
			accounts[i-1] = Integer.parseInt(keywords[i]);

		return "" + banco.totalBalance(accounts);
	}

	private String transfer(String[] keywords) {
		String r = "Success!";
		int from = Integer.parseInt(keywords[1]);
		int to = Integer.parseInt(keywords[2]);
		float amount = Float.parseFloat(keywords[3]);

		try {
			banco.transfer(from, to, amount);
		} catch(InvalidAccount | NotEnoughFunds e) {
			r = e.getClass().toString().split(" ")[1];
		}

		return r;
	}

	private String readLine() {
		String linha = null;

		try {
			linha = in.readLine();
		} catch(IOException e) {
			System.out.println("Oops");
		}

		return linha;
	}
}
