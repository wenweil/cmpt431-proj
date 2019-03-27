
    
 // Java implementation of  Server side 
 // It contains two classes : Server and ClientHandler 
 // Save file as Server.java 

 import java.io.DataInputStream;
 import java.io.DataOutputStream;
 import java.io.IOException;
 import java.net.ServerSocket;
 import java.net.Socket;
 import java.util.ArrayList;
 import java.util.List;
   
 // Server class 
 public class Server  
 { 
	private List<DataOutputStream> clients = new ArrayList<DataOutputStream>();
	static ServerSocket serSock = null;
		
		
	synchronized List<DataOutputStream> getClients() { return clients; }
	    
	synchronized void removeFromClients(DataOutputStream remoteOut) {
		clients.remove(remoteOut);
	}
		
	
	 
	 
	 
	 
     public static void main(String[] args) throws IOException  
     { 
 
    	 serSock = new ServerSocket(8889); 
           Server server = new Server();
           server.begin();
           

     } 
     
     public void begin() throws IOException {
         // running infinite loop for getting 
         // client request 
    	 
         while (true)  
         { 
             Socket client = null; 
               
             try 
             { 
                 // socket object to receive incoming client requests 
            	 client = serSock.accept(); 
                   
                 System.out.println("A new client is connected : " + client); 
                   
                 // obtaining input and out streams 
                 DataInputStream dis = new DataInputStream(client.getInputStream()); 
                 DataOutputStream dos = new DataOutputStream(client.getOutputStream()); 
                   
                 System.out.println("Assigning new thread for this client"); 
   
                 // create a new thread object 
                 clients.add(dos);
                 Thread t = new ClientHandler(client, dis, dos, this); 
   
                 // Invoking the start() method 
                 t.start(); 
                   
             } 
             catch (Exception e){ 
            	 client.close(); 
                 e.printStackTrace(); 
             } 
         } 
     }
 } 



