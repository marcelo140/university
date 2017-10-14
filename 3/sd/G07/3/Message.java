import java.net.*;

public class Message {
	private Socket sender;
	private String content;

	public Message(Socket sender, String content) {
		this.sender = sender;
		this.content = content;
	}

	public Socket sender() {
		return sender;
	}

	public String toString() {
		return content;
	}
}
