package Game.Game;

import Game.Packets.MutexRequestPacket;
import Game.Packets.SquareStringRequest;
import Game.Server.GameData;
import Game.Server.Server;
import Game.Strategies.stamps;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import Game.Main;
import Game.Client.Client;

import static Game.Utilities.Utilities.convertObjectToBytes;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Game {
    private final Pane root;

    private HashMap<String,Square> squares;

    private boolean[][] grid;


    private int brushSize = 3;
    private Color usrClr;

    private int squareSize = 50;

    private int numSqInRow  = -1;
    private int numSqInCol = -1;

    private HashMap<String,netMutex> mutex;

    private Double threashhold;

    public ArrayBlockingQueue<SquareStringRequest> requestPakets;

    private Server s = null;
    
    private String UserID;

    public void serverStart(){
    	s = Server.getInstance();
        s.setGameData(new GameData(brushSize,threashhold, Main.boardHeight,Main.boardWidth,Main.numBox));
        setupMutex();
        Thread thread = new Thread(s.getListener());
        Thread thread1 = new Thread(s.getSender());
        thread.start();
        thread1.start();
        
    }

    public Game(Pane root){
        squares = new HashMap<>();
        Client.getInstance();
		requestPakets = new ArrayBlockingQueue<SquareStringRequest>(2048);
        
        byte[] array = new byte[7]; // length is bounded by 7
        
        Random rand = new Random();

        rand.nextBytes(array);

        String generatedString = new String(array, Charset.forName("UTF-8"));
        
        usrClr = new Color(rand.nextDouble(),rand.nextDouble(),rand.nextDouble(),1);
        
        this.UserID = generatedString;
        
        this.root = root;
        
    }
    
    public void populateWIthRequestData() throws InterruptedException {
        for(int rowNumber = 0; rowNumber < numSqInCol; rowNumber++)
            for(int colNumer = 0; colNumer < numSqInRow; colNumer++){
                SquareStringRequest ssr = new SquareStringRequest(colNumer,rowNumber);
                requestPakets.put(ssr);
            }
    }

    private void setupMutex(){
        mutex = new HashMap<>();
        for (String s: squares.keySet()){
            netMutex m = new netMutex();
            mutex.put(s,m);
        }
    }

    public void addSquare(String squareID, int col, int row){

        int offsetX = 10 + 63 * col;
        int offsetY = 10 + 63 * row;

        Square square = new Square(offsetX,offsetY,squareSize,squareSize,this);
        squares.put(squareID,square);

        if(!root.getChildren().contains(square.getImage())){
            root.getChildren().addAll(square.getImage());
        }



    }


    public void initializeGrid(){
        for(int row = 0; row < numSqInCol; row++)
            for(int col = 0; col < numSqInRow; col++){
                grid[row][col] = false;
            }
    }

    public boolean isFilled(){
        for(int row = 0; row < numSqInCol; row++)
            for(int col = 0; col < numSqInRow; col++){
                if(grid[row][col] == false){
                    return false;
                };
            }
        return true;
    }

    public SquareStringRequest requestCrds(){
        SquareStringRequest sq = new SquareStringRequest(-1,-1);

        for(int row = 0; row < numSqInCol; row++)
            for(int col = 0; col < numSqInRow; col++){
                if(grid[row][col] == false){
                    return new SquareStringRequest(col,row);
                };
            }
        return sq;
    }

    public boolean getLock(String squareID) {
    	
    	if (s != null) {
    		System.out.print(squareID+" ");
    		return mutex.get(squareID).reqLock(UserID);
    	}
    	
    	Serializable r = (Serializable)new MutexRequestPacket(squareID,UserID);
    	
    	byte[] data;
		try {
			data = convertObjectToBytes(stamps.MUTEXLOCKREQUEST.val(),r);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	
    	DatagramPacket p;
		try {
			p = new DatagramPacket(data,data.length,InetAddress.getByName(Main.serverIP),Main.serverPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
    	
    	try {
			Client.outgoingPackets.put(p);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
    	//todo add getlock when server is not running
    	return true;

    }

    public boolean reqUnlock (String squareID){
    	
    	if (s != null) {
    		System.out.print(squareID+" ");
    		return mutex.get(squareID).reqUnlock(UserID);
    	}
    	
    	
        return true;
    }

    public void start() {
        squares = new HashMap<>();
        Square s;

        for (int i = 0; i < numSqInRow; i++) {
            for (int j = 0; j < numSqInCol; j++) {
                s = new Square(10 + 63 * i, 10 + 63 * j, squareSize, squareSize, this);

                byte[] array = new byte[7]; // length is bounded by 7

                new Random().nextBytes(array);

                String generatedString = new String(array, Charset.forName("UTF-8"));
                s.setEntityID(generatedString);

                squares.put(generatedString, s);
                root.getChildren().addAll(s.getImage());
            }
        }
    }



    public void startOld() {
        squares = new HashMap<>();
        int numInRow = 20;
        int numInCol = 14;
        Square s;

        for (int i = 0; i < numInRow; i++) {
            for (int j = 0; j < numInCol; j++) {
                s = new Square(10 + 63 * i, 10 + 63 * j, squareSize, squareSize, this);

                byte[] array = new byte[7]; // length is bounded by 7
                new Random().nextBytes(array);

                String generatedString = new String(array, Charset.forName("UTF-8"));
                s.setEntityID(generatedString);

                squares.put(generatedString, s);
                root.getChildren().addAll(s.getImage());
            }
        }
        setupMutex();
    }
    
    public void sendClaimedState(String EID) {
    	if (this.s != null) {
    		//todo broadcast this message to every client
    	}else {
    		//todo send message to server
    	}
    }
    
    public void sendTwoPointsforDrawLine(double x1, double y1, double x2, double y2, String entityID) {
		if (this.s != null) {
    		// todo broadcast this message to every client
    	}else {
    		//todo send message to server
    	}
		
	}
    
    public void sendDrawFail(String EID) {
    	if (this.s != null) {
    		//todo broadcast this message to every client
    	}else {
    		//todo send message to server
    	}
    }


    public void setNumSqInRow(int numSqInRow) {
        this.numSqInRow = numSqInRow;
    }

    public void setNumSqInCol(int numSqInCol) {
        this.numSqInCol = numSqInCol;
    }

    public Double getThreashhold(){
        return threashhold;
    }

    public void setThreashhold(Double in){
        this.threashhold = in;
    }


    public Color getUsrClr() {
        return usrClr;
    }

    public void setBrushSize(int sz){
        this.brushSize = sz;
    }

    public int getBrushSize(){
        return brushSize;
    }

    public HashMap<String,Square> getSquare(){
        return squares;
    }

	

}
