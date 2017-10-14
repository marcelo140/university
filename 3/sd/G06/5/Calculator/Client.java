import java.net.*;
import java.io.*;

public class Client extends Thread {
	private Socket cli;
	private BufferedReader in;
	private PrintWriter out;

	Client(Socket cli) throws IOException {
		this.cli = cli;
		in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
		out = new PrintWriter(cli.getOutputStream());
	}

	public void run() {
		System.out.println(cli.getInetAddress() + " connected");

		try {
			calculate(in, out);
			cli.close();
		} catch(IOException e) {
			System.out.println("Oops");
		}

		System.out.println(cli.getInetAddress() + " disconnected");
	}

	private void calculate(BufferedReader in, PrintWriter out) throws IOException {
		float sum = 0, counter = 0;
		String line = null;

		while((line = in.readLine()) != null) {
			float num = Integer.parseInt(line);

			sum += num;
			counter += 1;

			out.println(sum);
			out.flush();
		}

		out.println(sum/counter);
		out.flush();
	}
}
