import java.net.*;
import java.io.*;

public class Client extends Thread {
	private Socket cli;
	private BufferedReader in;
	private PrintWriter out;

	Client(Socket cli) throws IOException {
		this.cli= cli;
		in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
		out = new PrintWriter(cli.getOutputStream());
	}

	public void run() {
		System.out.println(cli.getInetAddress() + " connected");

		try {
			echoClient(in, out);
			cli.close();
		} catch (IOException e) {
			System.out.println("Oops");
		}

		System.out.println(cli.getInetAddress() + " disconnected");
	}

	private void echoClient(BufferedReader in, PrintWriter out) throws IOException {
		String message = null;

		while((message = in.readLine()) != null) {
			out.println(message);
			out.flush();
		}
	}
}
