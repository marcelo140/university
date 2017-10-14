import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

class Server {
    public static void main(String[] args) throws IOException {
        List<Socket> clients = new ArrayList<>();
        ServerSocket listener = new ServerSocket(5000);

        while(true) {
            Socket client = listener.accept();
            synchronized(clients) {
                clients.add(client);
            }

            Broadcaster bc = new Broadcaster(clients, client);
            bc.start();
        }
    }
}
