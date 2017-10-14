import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Listener extends Thread {
    SuperQueue queue; 
    Socket client;
    ActiveToken token;

    Listener(Socket client, SuperQueue queue, ActiveToken token) {
        this.client = client;
        this.queue = queue;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream())
            ); 

            while(token.isActive()) {
                String message = in.readLine(); 
                queue.write(message);
            }
        } catch(IOException e) {
           token.deactivate();
        }
    }
}
