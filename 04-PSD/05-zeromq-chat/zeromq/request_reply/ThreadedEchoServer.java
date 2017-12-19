package request_reply;
import org.zeromq.ZMQ;

class EchoWorker extends Thread {
  ZMQ.Context context;
  int n;
  EchoWorker(ZMQ.Context context, int n) { this.context = context; this.n = n; }
  public void run() {
    ZMQ.Socket socket = context.socket(ZMQ.REP);
    socket.connect("inproc://workers");
    while (true) {
      byte[] b = socket.recv();
      String s = new String(b);
      System.out.println("Worker " + n + " received " + s);
      try { Thread.sleep(1000); } catch (Exception e) {} // simulate working
      String res = s.toUpperCase();
      socket.send(res);
    }
    //socket.close();
  }
}

public class ThreadedEchoServer {
  public static void main(String[] args) {
    int nWorkers = Integer.parseInt(args[1]);
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket clients = context.socket(ZMQ.ROUTER);
    clients.bind("tcp://*:"+args[0]);
    ZMQ.Socket workers = context.socket(ZMQ.DEALER);
    workers.bind("inproc://workers");
    for (int i = 0; i < nWorkers; i++)
      new EchoWorker(context, i).start();
    ZMQ.proxy(clients, workers, null);
    // unreachable
    clients.close();
    workers.close();
    context.term();
  }
}

