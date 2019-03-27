import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
//ClientHandler class 
class ClientHandler extends Thread  
{ 
 
private Socket socket;
private DataOutputStream dos;
private DataInputStream dis;
private Server server;
private String user;
   

 // Constructor 
 public ClientHandler(Socket socket  ,DataInputStream dis, DataOutputStream dos,Server server)  
 { 
     this.socket = socket; 
     this.dis = dis; 
     this.dos = dos; 
     this.server = server;

 } 

 @Override
 public void run()  
 { 
     String received;
     

     try {
         while (true) {
        
        	 received = dis.readUTF();
        	 System.out.println(received);
             broadcast(received);
         }
     } 
	catch (IOException e) {
	    broadcast("User " + user + " disconnected");
	    server.removeFromClients(dos);
     } 
	finally {
		 System.out.println("1");
         try { cleanUp(); } 
	    catch (IOException x) { }
     }
 }
 
 // Send the message to all the sockets connected to the server.
 private void broadcast(String s) {
    List<DataOutputStream> clients = server.getClients();
    DataOutputStream dataOut = null;
    System.out.println(clients);
    
    Iterator<DataOutputStream> i = clients.iterator();
    while (i.hasNext() ) {
	   dataOut = (DataOutputStream)(i.next());
	   try { dataOut.writeUTF(s);} 
	   catch (IOException x) {
	       System.out.println(x.getMessage() +
				  ": Failed to broadcast to client.");
	       server.removeFromClients(dataOut);
	   }
    }
 }
 
 private void cleanUp() throws IOException {
     if (dos != null) {
        server.removeFromClients(dos);
        dos.close();
     }

     if (dis != null) {
        dis.close();
     }

     if (socket != null) {
        socket.close();
     }
  }
 
 
} 