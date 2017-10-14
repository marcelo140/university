import java.io.*;
import java.net.*;
import java.util.List;

class Broadcaster extends Thread {
    private List<Socket> clients;
    private Socket client;

    Broadcaster(List<Socket> clients, Socket client) {
        this.clients = clients;
        this.client = client;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));

            while(true) {
                String message = in.readLine();

                synchronized (clients) {
                    for (Socket peer: clients) {
                        PrintWriter out = new PrintWriter(peer.getOutputStream());
                        out.println(message);
                        out.flush();
                    }
                }
            }
        }catch(Exception e) {
            System.out.println(e);
        }
    }
}
