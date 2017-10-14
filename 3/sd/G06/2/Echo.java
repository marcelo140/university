import java.net.*;
import java.io.*;
import java.util.*;

public class Echo {
	public static void main(String[] args) throws IOException, UnknownHostException {
		// Ligar ao servidor atráves do endereço e porta dadas
		Socket cli = new Socket(args[0], Integer.parseInt(args[1]));
		// Obter streams de carateres input/output
		BufferedReader in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
		PrintWriter out = new PrintWriter(cli.getOutputStream());

		Scanner stdin = new Scanner(System.in);

		while(stdin.hasNext()) {
			// ler do stdin
			String line = stdin.next();

			// enviar a mensagem lida para o servidor
			out.println(line);
			out.flush();

			// Escrever no stdout resposta do servidor
			line = in.readLine();
			System.out.println(line);
		}
	}
}
