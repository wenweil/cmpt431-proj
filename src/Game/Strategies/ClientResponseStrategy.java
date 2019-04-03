package Game.Strategies;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;

public interface ClientResponseStrategy {
  DatagramPacket generateClientResponse(InetAddress serverAddress,int serverPort,Serializable requestObject);
}
