import java.net.*;
import java.io.*;

public class Reader extends Thread {
	private Hub hub;
	private Socket sock;
	private BufferedReader in;

	Reader(Hub hub, Socket sock) throws IOException {
		this.sock = sock;
		this.hub = hub;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}

	public void run() {
		String message = null;

		while((message = readMessage()) != null)
			hub.send(sock, message);

		closeConnection();
	}

	private void closeConnection() {
		try {
			sock.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println(sock + " disconnected");
	}

	private String readMessage() {
		String linha = null;

		try {
			linha = in.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return linha;
	}
}
