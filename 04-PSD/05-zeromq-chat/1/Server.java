import org.zeromq.ZMQ;

public class Server {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.PUB);

        socket.bind("tcp://*:5000");

        socket.close();
        context.term();
    }
}
