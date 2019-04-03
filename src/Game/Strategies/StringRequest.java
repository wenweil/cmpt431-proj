package Game.Strategies;

import Game.Client.Client;
import Game.Packets.ConnectionRequestPacket;
import Game.Packets.SquareStringRequest;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;

import static Game.Utilities.Utilities.convertObjectToBytes;

public class StringRequest {
  public DatagramPacket generateClientRequest(SquareStringRequest ssr){

    byte[] data = new byte[0];

    try {

      Serializable r = (Serializable) ssr;

      data = convertObjectToBytes((byte) 3, r);


    } catch (IOException e) {
      e.printStackTrace();
    }

    InetAddress address = Client.getInstance().getServerAddress();
    int port = Client.getInstance().getServerPort();

    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

    return packet;

  }

}
