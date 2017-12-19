import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public Map<String, User> users;
    public SuperQueue queue;

    public static void main(String[] args) throws IOException {
        final Server s = new Server();
        ServerSocket listener = new ServerSocket(5000);
        s.users = Collections.synchronizedMap(new HashMap<>());
        s.queue = new SuperQueue(100);

        while(true) {
            Socket socket = listener.accept();
            Authenticator auth = new Authenticator(s, socket);
        }
    }
}
