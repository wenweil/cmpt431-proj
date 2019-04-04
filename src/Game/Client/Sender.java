package Game.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

import static Game.Client.Client.outgoingPackets;

public class Sender implements Runnable {
  public void run() {
    try {
      while (true) {
        DatagramPacket packet =  outgoingPackets.take();
        Client.getInstance().getClientSocket().send(packet);

      }
    }
    catch (InterruptedException| IOException e){e.printStackTrace();}
  }
}
