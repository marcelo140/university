import java.net.*;
import java.io.*;

public class Echod {
	public static void main(String[] args) throws IOException {
		ServerSocket srv = new ServerSocket(5000);

		while(true) {
			Socket cli = srv.accept();
			Thread client = new Client(cli);
			client.start();
		}
	}
}
