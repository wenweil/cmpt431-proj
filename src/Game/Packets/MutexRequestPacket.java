package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MutexRequestPacket implements Serializable {

  public String getSquareCode() {
    return squareCode;
  }

  private String squareCode, UserID;

  public MutexRequestPacket(String squareCode,String UserID){
    this.squareCode = squareCode;
    this.UserID = UserID;
  }

  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
  }
}
