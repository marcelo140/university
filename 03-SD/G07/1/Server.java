import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket srv = new ServerSocket(5001);
		Accumulator acc = new Accumulator();

		while(true) {
			Socket cli = srv.accept();
			Thread client = new Client(cli, acc);
			client.start();
		}
	}
}
