package Game.Server;

import Game.Packets.ConnectionResponsePacket;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;

public class GameData {

  private int brushSize;

  private int boardWidth;
  private int boardHeight;

  private int numSqInRow;
  private int numSqInCol;

  private double threshold;
  
  public HashMap<String, SquareStatus> getSquareStatus() {
	    return squareStatus;
	  }

  private HashMap<String,SquareStatus> squareStatus;

  private String[][] squareCodes = new String[10][10];
  private HashMap<String,Mutex> mutexes;

  public int getBrushSize() {
    return brushSize;
  }

  public int getBoardWidth() {
    return boardWidth;
  }

  public int getBoardHeight() {
    return boardHeight;
  }

  public int getNumSqInRow() {
    return numSqInRow;
  }

  public int getNumSqInCol() {
    return numSqInCol;
  }
  
  public double getThreshold() {
	  return threshold;
  }
  
  public HashMap<String, Mutex> getMutexes() {
	    return mutexes;
	  }

  public GameData (int brushSize,double threshold,int boardHeight,int boardWidth , int numBox){
    this.brushSize = brushSize;
    this.threshold = threshold;
    this.boardHeight = boardHeight;
    this.boardWidth = boardWidth;
    this.numSqInCol = numBox;
    this.numSqInRow = numBox;
    this.squareCodes = new String[this.numSqInCol][this.numSqInRow];
    mutexes = new HashMap<>();
    squareStatus = new HashMap<>();
    
  }

  public GameData(){

    brushSize = 2;

    boardWidth = 1600;
    boardHeight = 900;

    numSqInRow = 20;
    numSqInCol = 14;

    this.squareCodes = new String[14][20];

    // True mutex is available false it is not;
    mutexes = new HashMap<>();

    genSqrCodes();
  }

  private void genSqrCodes(){
    for(int row = 0; row < numSqInCol; row++)
      for(int col = 0; col < numSqInRow; col++){

        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);

        String generatedString = new String(array, Charset.forName("UTF-8"));

        squareCodes[row][col] = generatedString;
      }
  }
  
  public void setSqrCodes(int row, int col,String s) {
	  squareCodes[row][col] = s;
  }

  public String getSquareCode(int row, int col){
    return squareCodes[row][col];
  }

  public ConnectionResponsePacket getGameData(){
    ConnectionResponsePacket response = new ConnectionResponsePacket();

    response.setBoardHeight(boardHeight);
    response.setBoardWidth(boardWidth);
    response.setBrushSize(brushSize);
    response.setNumSqInColumn(numSqInCol);
    response.setNumSqInRow(numSqInRow);
    response.setThreshold(threshold);

    return response;
  }


}
