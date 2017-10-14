import java.net.*;
import java.io.*;

public class Writer extends Thread {
	private Hub hub;
	private Socket sock;
	private PrintWriter out;
	private int nextMessage;

	Writer(Hub hub, Socket sock) throws IOException {
		this.hub = hub;
		this.sock = sock;
		out = new PrintWriter(sock.getOutputStream());
		nextMessage = hub.nextMessage();
	}

	public void run() {
		while(!sock.isClosed()) {
			Message msg = receive();

			if (msg.sender() == sock)
				continue;

			out.println(msg);
			out.flush();
		}
	}

	private Message receive() {
		Message msg = null;

		try {
			msg = hub.receive(nextMessage);
			nextMessage += 1;
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}

		return msg;
	}
}
