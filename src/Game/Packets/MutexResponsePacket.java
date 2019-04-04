package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MutexResponsePacket implements Serializable {

  private String squareCode;
  private Boolean lock;

  public MutexResponsePacket(){

  }

  public MutexResponsePacket(String squareCode,Boolean lock){

    this.squareCode = squareCode;
    this.lock = lock;
  }

  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
  }


  public String getSquareCode() {
    return squareCode;
  }

  public void setSquareCode(String squareCode) {
    this.squareCode = squareCode;
  }

  public Boolean getLock() {
    return lock;
  }

  public void setLock(Boolean lock) {
    this.lock = lock;
  }

}