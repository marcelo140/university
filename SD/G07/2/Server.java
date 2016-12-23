import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket srv = new ServerSocket(5002);
		Banco banco = new Banco();

		while(true) {
			Socket cliSocket = srv.accept();
			Thread cli = new Client(cliSocket, banco);
			cli.start();
		}
	}
}
