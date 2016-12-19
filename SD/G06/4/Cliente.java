import java.util.*;
import java.net.*;
import java.io.*;

public class Cliente {
	public static void main(String[] args) throws IOException {
		Socket cli = new Socket(args[0], Integer.parseInt(args[1]));
		BufferedReader in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
		PrintWriter out = new PrintWriter(cli.getOutputStream());
		Scanner stdin = new Scanner(System.in);

		while(stdin.hasNext()) {
			String linha = stdin.next();

			out.println(linha);
			out.flush();

			linha = in.readLine();
			System.out.println(linha);
		}

		cli.shutdownOutput();

		String media = in.readLine();
		System.out.println("\nMédia: " + media);
	}
}
