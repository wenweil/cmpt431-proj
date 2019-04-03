package Game.Strategies;

import Game.Client.Client;
import Game.Packets.ConnectionRequestPacket;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;

import static Game.Utilities.Utilities.convertObjectToBytes;

public class ConnectionRequest implements ClientRequestStrategy {

  @Override
  public DatagramPacket generateClientRequest() {

    byte[] data = new byte[0];

    try {

      Serializable r = (Serializable) new ConnectionRequestPacket(1234);

      data = convertObjectToBytes((byte) 1, r);


    } catch (IOException e) {
      e.printStackTrace();
    }

    InetAddress address = Client.getInstance().getServerAddress();
    int port = Client.getInstance().getServerPort();

    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

    return packet;

  }
}
