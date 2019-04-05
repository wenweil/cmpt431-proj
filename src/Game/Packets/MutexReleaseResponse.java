package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MutexReleaseResponse implements Serializable {

  public MutexReleaseResponse(Boolean signal) {
    this.lockStatus = signal;
  }

  public Boolean getLockStatus() {
    return lockStatus;
  }

  Boolean lockStatus;


  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
  }
}
