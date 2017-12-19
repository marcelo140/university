import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender extends Thread {
    SuperQueue queue; 
    Socket client;
    ActiveToken token;

    public Sender(Socket client, SuperQueue queue, ActiveToken token) {
        this.queue = queue;
        this.client = client;
        this.token = token;
    }

    public void run() {
        int nextMessage = queue.connect();

        try {
            PrintWriter out = new PrintWriter(client.getOutputStream());

            while(token.isActive()) { 
                try {
                    String message = queue.read(nextMessage);
                    nextMessage += 1;

                    out.println(message);
                    out.flush();
                } catch(MessageDeletedException e) {
                    nextMessage = queue.reconnect();
                    continue;
                }
            }
        } catch(IOException | InterruptedException e) {
            token.deactivate();
        }

        queue.disconnect(nextMessage);
    }
}
