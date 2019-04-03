package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MutexResponsePacket implements Serializable {

  private String squareCode;
  private Boolean lock;
  private String message;

  public MutexResponsePacket(){

  }

  public MutexResponsePacket(String squareCode,Boolean lock,String message){

    this.squareCode = squareCode;
    this.lock = lock;
    this.message = message;
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

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}