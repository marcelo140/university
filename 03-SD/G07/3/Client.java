import java.net.*;
import java.io.*;

public class Client {
	private Thread reader;
	private Thread writer;
	private Socket sock;

	Client(Hub hub, Socket sock) throws IOException {
		this.sock = sock;
		reader = new Reader(hub, sock);
		writer = new Writer(hub, sock);
	}

	public void start() {
		reader.start();
		writer.start();
		System.out.println(sock.getInetAddress() + " connected");
	}
}
