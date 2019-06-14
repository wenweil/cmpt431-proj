package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SquareStringResponse extends SquareStringRequest {

  private String code;

  public String getCode() {
    return code;
  }

  public SquareStringResponse(int col, int row, String code){
    super(col,row);
    this.code = code;
  }

  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
  }
}
