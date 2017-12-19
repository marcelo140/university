import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket srv = new ServerSocket(5003);
		Hub hub = new Hub();

		while(true) {
			Socket cliSocket = srv.accept();
			Client cli = new Client(hub, cliSocket);
			cli.start();
		}
	}
}
