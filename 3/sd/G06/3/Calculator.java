import java.io.*;
import java.net.*;

public class Calculator {
	public static void main(String[] args) throws IOException {
		ServerSocket srv = new ServerSocket(5001);

		while(true) {
			Socket cli = srv.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
			PrintWriter out = new PrintWriter(cli.getOutputStream());

			System.out.println(cli.getInetAddress() + " connected");
			calculate(in, out);
			cli.close();
			System.out.println(cli.getInetAddress() + " disconnected");
		}
	}

	private static void calculate(BufferedReader in, PrintWriter out) throws IOException {
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
