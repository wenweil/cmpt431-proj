package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SendStringPacket implements Serializable {
	
	private String s;
	
	public SendStringPacket(String s) {
		this.s = s;
	}
	
	public String getString() {
		return s;
	}
	
	private void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		in.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}

		 


}
