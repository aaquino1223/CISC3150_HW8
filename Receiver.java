import java.io.*;
import java.util.*;

//Class Receiver is used so that the PipedInputStream object
//can read on its own thread
public class Receiver implements Runnable {
	private PipedInputStream receiving;
	private boolean messageReceived;
	
	private Vector<Byte> byteVec;
	private byte[] bytes;
	private byte b;
	
	private Thread receiveThread;
	private boolean running;
	
	//1 PipedInputStream variable
	//Initializes variables
	public Receiver(PipedInputStream r) {
		receiving = r;
		byteVec = new Vector<Byte>();
		receiveThread = new Thread(this);
		running = true;
		messageReceived = true;
	}
	
	//The method used to retreive the bytes read
	//Returns if the message hasn't been received by the class user
	//and if it exists and null otherwise
	public Vector<Byte> receiveBytes() {
		if(byteVec.size() > 0 && !messageReceived) {
			messageReceived = true;
			return byteVec;
		}
		
		return null;
	}
	
	//Starts the receiveThread
	public void start() {
		receiveThread.start();
	}
	
	//Constantly reads, and if something has been sent
	//The message will be store into a vector of bytes
	@Override
	public void run() {
		while(running) {
			try {
				b = (byte)receiving.read();
				byteVec.clear();
				if(receiving.available() != 0) {
					do {
						byteVec.add(b);
						
						if(receiving.available() == 0) {
							break;
						}
						
						b = (byte) receiving.read();
					} while(true);
					messageReceived = false;
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}