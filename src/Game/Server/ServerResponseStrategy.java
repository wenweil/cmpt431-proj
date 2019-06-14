package Game.Server;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;

public interface ServerResponseStrategy {
  DatagramPacket generateServerResponse(InetAddress address,int port,Serializable requestObject) throws IOException;
}
