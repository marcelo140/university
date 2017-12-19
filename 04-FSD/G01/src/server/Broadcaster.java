package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Broadcaster extends Thread {
    private List<Socket> clients;
    private Socket client;

    Broadcaster(List<Socket> clients, Socket client) {
        this.clients = clients;
        this.client = client;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream())
            );

            while(!client.isClosed()) {
                String message = in.readLine();

                if (message == null) {
                    clients.remove(client);
                    client.close();
                    break;
                }

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
