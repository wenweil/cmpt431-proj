package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

public class SquareStringRequest implements Serializable {

  private int colNumber;
  private int rowNumber;

  public SquareStringRequest(int col, int row){
    this.colNumber = col;
    this.rowNumber = row;
  }

  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SquareStringRequest that = (SquareStringRequest) o;
    return colNumber == that.colNumber &&
        rowNumber == that.rowNumber;
  }

  @Override
  public int hashCode() {
    return Objects.hash(colNumber, rowNumber);
  }

  public int getColNumber() {
    return colNumber;
  }

  public void setColNumber(int colNumber) {
    this.colNumber = colNumber;
  }

  public int getRowNumber() {
    return rowNumber;
  }

  public void setRowNumber(int rowNumber) {
    this.rowNumber = rowNumber;
  }
}
