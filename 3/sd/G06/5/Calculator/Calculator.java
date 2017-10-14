import java.io.*;
import java.net.*;

public class Calculator {
	public static void main(String[] args) throws IOException {
		ServerSocket srv = new ServerSocket(5001);

		while(true) {
			Socket cli = srv.accept();
			Thread client = new Client(cli);
			client.start();
		}
	}
}
