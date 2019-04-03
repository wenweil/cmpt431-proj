package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConnectionRequestPacket implements Serializable {
  int clientPort;


  public ConnectionRequestPacket(int clientPort) {
    this.clientPort = clientPort;
  }

  public int getClientPort() {
    return clientPort;
  }

  public void setClientPort(int clientPort) {
    this.clientPort = clientPort;
  }

  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
  }
}
