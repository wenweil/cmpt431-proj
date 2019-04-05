package Game.Game;

import Game.Packets.ClientDrawingDataPacket;
import Game.Packets.MutexRequestPacket;
import Game.Packets.SendStringPacket;
import Game.Packets.SquareStringRequest;
import Game.Server.GameData;
import Game.Server.Mutex;
import Game.Server.Server;
import Game.Strategies.stamps;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import Game.Main;
import Game.Client.Client;
import Game.Server.SquareStatus;

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

    public static Boolean reqLock = false;

    public ArrayBlockingQueue<Tuple<String,InetAddress,Integer>> tuples;
    

    public class Tuple<X, Y ,Z> {
        public final X x;
        public final Y y;
        public final Z z;
        
        public Tuple(X x, Y y, Z z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public void serverStart(){
    	s = Server.getInstance();
       
        
        Thread thread = new Thread(s.getListener());
        Thread thread1 = new Thread(s.getSender());
        thread.start();
        thread1.start();
        
        squares = new HashMap<>();
        Square s;
        
        this.s.setGameData(new GameData(brushSize,threashhold, Main.boardHeight,Main.boardWidth,Main.numboxint));
        for (int i = 0; i < numSqInRow; i++) {
            for (int j = 0; j < numSqInCol; j++) {
                s = new Square(10 + 63 * i, 10 + 63 * j, squareSize, squareSize, this);

                byte[] array = new byte[7]; // length is bounded by 7

                new Random().nextBytes(array);
                
                
                String generatedString = new String(array, Charset.forName("UTF-8"));
                s.setEntityID(generatedString);
                
                this.s.getGameData().setSqrCodes(i, j, generatedString);
                this.s.getGameData().getSquareStatus().put(generatedString, SquareStatus.AVAILABLE);
                this.s.getGameData().getMutexes().put(generatedString, new Mutex());

                squares.put(generatedString, s);
                root.getChildren().addAll(s.getImage());
            }
        }
        setupMutex();
        
    }

    public Game(Pane root){
        squares = new HashMap<>();
		requestPakets = new ArrayBlockingQueue<SquareStringRequest>(2048);
		tuples = new ArrayBlockingQueue<Tuple<String,InetAddress,Integer>>(2048);
        
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
        square.setEntityID(squareID);
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
    
    public synchronized void sendClaimedState(String EID) {
    	if (this.s != null) {
    		//todo broadcast this message to every client
    		SendStringPacket p = new SendStringPacket(EID + ";"+ usrClr.toString());
    		byte stamp = stamps.DRAWCLAIM.val();
    		try {
				byte[] data = convertObjectToBytes(stamp,p);
				for(Tuple t : tuples) {
					try {
						InetAddress IP = (InetAddress)t.y;
						int port = (int) t.z;
						Server.outgoingQueue.put(new DatagramPacket(data,data.length,IP,port));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}else {
    		SendStringPacket p = new SendStringPacket(EID + ";"+ usrClr.toString());
    		byte stamp = stamps.DRAWCLAIM.val();
    		try {
				byte[] data = convertObjectToBytes(stamp,p);
				try {
					Client.outgoingPackets.put(new DatagramPacket(data,data.length,InetAddress.getByName(Main.serverIP),Main.serverPort));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//todo send message to server
    	}
    }
    
    public synchronized void sendTwoPointsforDrawLine(double x1, double y1, double x2, double y2, String entityID) {
		if (this.s != null) {
			ClientDrawingDataPacket p = new ClientDrawingDataPacket();
			p.setX1(x1);
			p.setX2(x2);
			p.setY1(y1);
			p.setY2(y2);
			p.setColor(usrClr);
			p.setEID(entityID);
			byte stamp = stamps.DRAWINGDATA.val();
			
			try {
				byte[] data = convertObjectToBytes(stamp,p);
				for(Tuple t : tuples) {
					try {
						InetAddress IP = (InetAddress)t.y;
						int port = (int) t.z;
						Server.outgoingQueue.put(new DatagramPacket(data,data.length,IP,port));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
    		// todo broadcast this message to every client
    	}else {
    		//todo send message to server
    		ClientDrawingDataPacket p = new ClientDrawingDataPacket();
			p.setX1(x1);
			p.setX2(x2);
			p.setY1(y1);
			p.setY2(y2);
			p.setColor(usrClr);
			p.setEID(entityID);
			byte stamp = stamps.DRAWINGDATA.val();
			
			try {
				byte[] data = convertObjectToBytes(stamp,p);
				try {
					Client.outgoingPackets.put(new DatagramPacket(data,data.length,InetAddress.getByName(Main.serverIP),Main.serverPort));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
		
	}
    
    
    
    public synchronized void sendDrawFail(String EID) {
    	
    
    	if (this.s != null) {
    		
    		SendStringPacket p = new SendStringPacket(EID);
    		byte stamp = stamps.DRAWFAIL.val();
    		try {
				byte[] data = convertObjectToBytes(stamp,p);
				for(Tuple t : tuples) {
					try {
						InetAddress IP = (InetAddress)t.y;
						int port = (int) t.z;
						Server.outgoingQueue.put(new DatagramPacket(data,data.length,IP,port));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//todo broadcast this message to every client
    	}else {
    		//todo send message to server
    		SendStringPacket p = new SendStringPacket(EID);
    		byte stamp = stamps.DRAWFAIL.val();
    		try {
    			byte[] data = convertObjectToBytes(stamp,p);
				try {
					Client.outgoingPackets.put(new DatagramPacket(data,data.length,InetAddress.getByName(Main.serverIP),Main.serverPort));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

    public netMutex getMutex(String EID){
        return mutex.get(EID);
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
    
    public String getusrID() {
    	return UserID;
    }

	public void Adduser(InetAddress address, int port, String string) {
		Tuple t = new Tuple<>(string,address,port);
		tuples.add(t);
		
	}

	public void sendBeingFilled(String EID) {
		if (this.s != null) {

			SendStringPacket p = new SendStringPacket(new Color(usrClr.getRed(),usrClr.getGreen(),usrClr.getBlue(),.5).toString()+"|"+EID);
			byte stamp = stamps.DRAWING.val();
			try {
				byte[] data = convertObjectToBytes(stamp,p);
				for(Tuple t : tuples) {
					try {
						InetAddress IP = (InetAddress)t.y;
						int port = (int) t.z;
						Server.outgoingQueue.put(new DatagramPacket(data,data.length,IP,port));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//todo broadcast this message to every client
		}else {
			//todo send message to server
			SendStringPacket p = new SendStringPacket(new Color(usrClr.getRed(),usrClr.getGreen(),usrClr.getBlue(),.5).toString()+"|"+EID);
			byte stamp = stamps.DRAWING.val();
			try {
				byte[] data = convertObjectToBytes(stamp,p);
				try {
					Client.outgoingPackets.put(new DatagramPacket(data,data.length,InetAddress.getByName(Main.serverIP),Main.serverPort));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	

}
