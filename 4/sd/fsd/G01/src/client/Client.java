package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws IOException {
		Socket cli = new Socket(args[0], Integer.parseInt(args[1]));

		PrintWriter out = new PrintWriter(cli.getOutputStream());
		Scanner stdin = new Scanner(System.in);

        ServerListener sl = new ServerListener(cli);
        sl.start();

		while(stdin.hasNextLine()) {
			String line = stdin.nextLine();

			out.println(line);
			out.flush();
		}
	}
}
