package Game.Server;

import java.io.IOException;
import java.net.DatagramPacket;

public class Sender implements Runnable {
  Server server;

  public Sender(Server server){
    this.server=server;
  }

  public void run(){

    while(true){
      try {
        DatagramPacket packet = Server.outgoingQueue.take();
        System.out.println("packet to client send");
        System.out.println(packet.getAddress()+" "+packet.getPort());
        Server.getInstance().getUdpSocket().send(packet);

      } catch (InterruptedException| IOException e) {
        e.printStackTrace();
      }

    }

  }
}
