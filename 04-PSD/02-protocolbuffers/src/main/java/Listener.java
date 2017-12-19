import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import messaging.Data.*;

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
        InputStream in = client.getInputStream();

        while(token.isActive()) {
            Message msg = Message.parseDelimitedFrom(in);
            queue.write(msg);
        }
        } catch(IOException e) {
           token.deactivate();
        }
    }
}
