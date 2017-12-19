import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import authentication.Protocol.*;

public class Authenticator extends Thread {
    private Server server;
    private Socket client;
    private int attempts;

    Authenticator(Server server, Socket socket) {
        this.server = server;
        this.client = client;
        this.attempts = 3;
    }

    public void run() {
        try {
        InputStream is = client.getInputStream();
        OutputStream os = client.getOutputStream();

        while(attempts > 0) {
            AuthenticationRequest req = AuthenticationRequest.parseDelimitedFrom(is);

            AuthenticationResponse res;
            if (req.getRequest().equals(AuthenticationRequest.RequestType.REGISTRATION)) {
                res = handleRegistration(req);
            } else {
                res = handleLogin(req);
            }

            res.writeDelimitedTo(os);
            os.flush();
        }
        } catch(IOException e) {
            return;
        }
    }

    private AuthenticationResponse handleRegistration(AuthenticationRequest req) {
        if (server.users.containsKey(req.getUsername())) {
            return AuthenticationResponse.newBuilder()
                .setResponse(AuthenticationResponse.ResponseType.ERROR)
                .setDescription("Username already exists")
                .build();
        }

        server.users.put(req.getUsername(), new User(req.getUsername(), req.getPassword()));

        return AuthenticationResponse.newBuilder()
            .setResponse(AuthenticationResponse.ResponseType.OK)
            .setDescription("User registered with success")
            .build();
    }

    private AuthenticationResponse handleLogin(AuthenticationRequest req) {
        User user = server.users.get(req.getUsername());

        if (user == null || !user.authenticate(req.getPassword())) {
            attempts -= 1;

            return AuthenticationResponse.newBuilder()
                .setResponse(AuthenticationResponse.ResponseType.ERROR)
                .setDescription("username/password invalid ")
                .build();
        }

        ActiveToken token = new ActiveToken();
        Listener l = new Listener(client, server.queue, token);
        Sender s = new Sender(client, server.queue, token);

        l.start();
        s.start();

        attempts = 0;
        return AuthenticationResponse.newBuilder()
            .setResponse(AuthenticationResponse.ResponseType.OK)
            .setDescription("Authentication successfull")
            .build();
    }
}
