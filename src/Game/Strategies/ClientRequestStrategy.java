package Game.Strategies;

import java.net.DatagramPacket;

public interface ClientRequestStrategy {
  DatagramPacket generateClientRequest();
}
