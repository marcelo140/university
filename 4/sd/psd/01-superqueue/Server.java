import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(5000);
        SuperQueue queue = new SuperQueue(100);

        while(true) {
            Socket socket = listener.accept();

            ActiveToken token = new ActiveToken();
            Listener l = new Listener(socket, queue, token);
            Sender s = new Sender(socket, queue, token);

            l.start();
            s.start();
        }
    }
}
