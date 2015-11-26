import java.io.*;
import java.util.*;
import java.text.*;

//ClientB class sends 4 predetermined messages at 5 second intervals
//It sets up a Receiver to constantly read for any messages that pop up
//Messages sent and received are printed
public class ClientB implements Runnable {
	
	private PipedInputStream receiving;
	private Receiver receiver;
	
	private PipedOutputStream sending;
	private String[] randMsgs;
	private int next;

	private Vector<Byte> byteVec;
	private byte[] bytes;

	private Thread clientThread;
	private boolean running;

	private Date date;
	private long intervalCondition;
	
	//Initializes variables
	public ClientB() {
		receiving = new PipedInputStream();
		receiver = new Receiver(receiving);
		sending = new PipedOutputStream();
		randMsgs = new String[] {"Woot!", "Oh yes!", "How are you?", "The cake is not a lie"};
		clientThread = new Thread(this);
		running = true;
		next = 0;
	}
	//For connecting purposes
	//Returns receiving
	public PipedInputStream getInputStream() {
		return receiving;
	}
	
	//For connecting purposes
	//Returns sending
	public PipedOutputStream getOutputStream() {
		return sending;
	}
	
	//Initializes intervalCondition for sending messages
	//Starts the receiver to ensure constant reading
	//And then it starts the client thread
	public void start() {
		intervalCondition = new Date().getSeconds() + 5;
		receiver.start();
		clientThread.start();
	}
	
	//Constantly checks to see if something has been read
	//If something has been read a vector of bytes will pop up
	//Sends random messages
	@Override
	public void run() {
		while (running) {
			try {
				byteVec = receiver.receiveBytes();
				if(byteVec != null) {
					date = new Date();
					
					bytes = new byte[byteVec.size()];
						
					for(int i = 0; i < bytes.length; i++) {
						bytes[i] = byteVec.elementAt(i).byteValue();
					}
					
					System.out.println("At " + DateFormat.getDateTimeInstance().format(date) + " Client B received: " + new String(bytes));
				}
				date = new Date();
				if(new Date().getSeconds() == intervalCondition) {
					intervalCondition += 5;
					if(next != 4) {
						System.out.println("At " + DateFormat.getDateTimeInstance().format(date) + " Client B said: " + randMsgs[next]);
						sending.write(randMsgs[next++].getBytes());
						sending.flush();
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}