package request_reply;
import org.zeromq.ZMQ;

public class EchoServer {
  public static void main(String[] args) {
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket socket = context.socket(ZMQ.REP);
    for (String s : args)
      socket.bind("tcp://*:" + s);
    while (true) {
      byte[] b = socket.recv();
      String s = new String(b);
      System.out.println("Received " + s);
      String res = s.toUpperCase();
      socket.send(res);
    }
    //socket.close();
    //context.term();
  }
}

