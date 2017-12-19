package request_reply;
import org.zeromq.ZMQ;

public class EchoClient {
  public static void main(String[] args) {
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket socket = context.socket(ZMQ.REQ);
    socket.connect("tcp://localhost:"+args[0]);
    while (true) {
      String s = System.console().readLine();
      if (s == null) break;
      socket.send(s);
      byte[] b = socket.recv();
      System.out.println(new String(b));
    }
    socket.close();
    context.term();
  }
}

