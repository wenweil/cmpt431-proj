package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConnectionResponsePacket implements Serializable {

  int boardWidth;
  int boardHeight;

  int numSqInRow;
  int numSqInColumn;

  int brushSize;

  double threshold;

  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
  }

  public int getBoardWidth() {
    return boardWidth;
  }

  public void setBoardWidth(int boardWidth) {
    this.boardWidth = boardWidth;
  }

  public int getBoardHeight() {
    return boardHeight;
  }

  public void setBoardHeight(int boardHeight) {
    this.boardHeight = boardHeight;
  }

  public int getNumSqInRow() {
    return numSqInRow;
  }

  public void setNumSqInRow(int numSqInRow) {
    this.numSqInRow = numSqInRow;
  }

  public int getNumSqInColumn() {
    return numSqInColumn;
  }

  public void setNumSqInColumn(int numSqInColumn) {
    this.numSqInColumn = numSqInColumn;
  }

  public int getBrushSize() {
    return brushSize;
  }

  public void setBrushSize(int brushSize) {
    this.brushSize = brushSize;
  }

  public double getThreshold() {
    return threshold;
  }

  public void setThreshold(double threshold) {
    this.threshold = threshold;
  }
}
