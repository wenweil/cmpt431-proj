package Game.Client;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Game.Main;

public class Client{

  private static Client instance = null;
  public static BlockingQueue<DatagramPacket> outgoingPackets = new ArrayBlockingQueue<>(512);
  private ExecutorService threadExecutor = Executors.newFixedThreadPool(1000);
  // create static client instance
  

  // method to retrieve the instance
  public static Client getInstance(){
	if (instance == null) {
	    try {
	      instance = new Client(Main.serverIP,Main.serverPort,Math.abs(new Random().nextInt()%62535+3000));
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
    return instance;
  }

  public void broadcast(byte[] data){

  }


  public InetAddress getServerAddress() {
    return serverAddress;
  }

  // Private fields for socket and server Address;
  private DatagramSocket clientSocket;
  private InetAddress serverAddress;
  private int serverPort;


  public DatagramSocket getClientSocket() {
    return clientSocket;
  }

  public int getServerPort() {
    return serverPort;
  }
  public static int counter =0;
  // Client that sends generated values to the
  private Client(String serverAddress, int serverPort, int localPort) throws SocketException, UnknownHostException {

    clientSocket = new DatagramSocket(localPort);
    this.serverAddress = InetAddress.getByName(serverAddress);
    this.serverPort = serverPort;
    
    // start listner and sender threads;
    new Thread(new Listener()).start();
    new Thread(new Sender()).start();
    Timer t = new Timer();
    TimerTask tt = new TimerTask() {
    	@Override
    	public void run() {
    		SocketAddress socketAddress = new InetSocketAddress(serverAddress, serverPort);
    		Socket socket = new Socket();
    		int timeout = 1000;
    		
    		try {
    			socket.connect(socketAddress, timeout);
    			if(socket.getInetAddress().isReachable(timeout)) {
    				counter = 0;
    			}
    			socket.close();   			
    		} catch(Exception e) {
    			counter++;
    			//e.printStackTrace();
    			//System.out.println(counter);
    			if (counter >= 5) {
    				System.out.println("CANNOT CONNECT TO SERVER");
    				//System.out.println("TEST : " + Main.game.tuples.size());
    			}
    			
    		}
    		
    	}
    };
    
    t.scheduleAtFixedRate(tt, 0, 2000);

  }
  
  public ExecutorService getThreadExecutor() {
	    return threadExecutor;
	  }

  }