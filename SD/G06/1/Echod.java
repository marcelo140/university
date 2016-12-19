import java.net.*;
import java.io.*;

public class Echod {
	public static void main(String[] args) throws IOException {
		ServerSocket srv = new ServerSocket(5000);

		while(true) {
			// Aceitar ligação do cliente
			Socket cli = srv.accept();
			// Obtér streams de carateres input/output
			BufferedReader in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
			PrintWriter out = new PrintWriter(cli.getOutputStream());

			System.out.println(cli.getInetAddress() + " connected");
			echoClient(in, out);
			cli.close();
			System.out.println(cli.getInetAddress() + " disconnected");
		}
	}

	private static void echoClient(BufferedReader in, PrintWriter out) throws IOException {
		String message = null;

		// Enquanto o cliente não enviar EOF dar echo de todas as mensagens recebidas
		while((message = in.readLine()) != null) {
			out.println(message);
			out.flush();
		}
	}
}
