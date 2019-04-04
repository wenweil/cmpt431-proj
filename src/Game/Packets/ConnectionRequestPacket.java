package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConnectionRequestPacket implements Serializable {
  String userID;


  public ConnectionRequestPacket(String userID) {
    this.userID = userID;
  }

  public String getUID() {
    return userID;
  }

  public void setClientPort(String userID) {
    this.userID = userID;
  }

  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
  }
}
