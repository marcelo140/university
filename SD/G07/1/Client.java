import java.net.*;
import java.io.*;

public class Client extends Thread {
	private Accumulator acc;
	private Socket cli;
	private BufferedReader in;
	private PrintWriter out;

	Client(Socket cli, Accumulator acc) throws IOException {
		this.acc = acc;
		this.cli = cli;
		in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
		out = new PrintWriter(cli.getOutputStream());
	}

	public void run() {
		System.out.println(cli.getInetAddress() + " connected");

		try {
			calculate(in, out, acc);
			cli.close();
		} catch (IOException e) {
			System.out.println("Oops");
		}

		System.out.println(cli.getInetAddress() + " disconnected");
	}

	private void calculate(BufferedReader in, PrintWriter out, Accumulator acc)
		throws IOException {
		String line = null;
		float sum = 0;
		float counter = 0;

		while((line = in.readLine()) != null) {
			float num = Integer.parseInt(line);
			sum += num;
			counter += 1;
			float total = acc.increment(num);

			out.println(total);
			out.flush();
		}

		out.println(sum/counter);
		out.flush();
	}
}
