package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerListener extends Thread {
    Socket socket;

    ServerListener(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
             );

            while(!socket.isClosed()) {
                String line = in.readLine();

                if (line == null) {
                    socket.close();
                    break;
                }

                System.out.println(line);
            }
        } catch (IOException e) {
        }
    }
} 
