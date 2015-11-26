import java.io.*;
import java.util.*;

//Class ChatServer will serve as a medium for the chat software
//Clients will be send messages to each other through this class
public class ChatServer implements Runnable {
		
		private PipedInputStream fromA, fromB;
		private Receiver receiverA, receiverB;
		
		private PipedOutputStream toA, toB;
		
		private Vector<Byte> byteVec;
		private byte[] bytes;
		
		private Thread serverThread;
		private boolean running;
		
		public ChatServer() {
			serverThread = new Thread(this);
			running = true;
		}
		
		//1 ClientA variable and 1 ClientB variable
		//Creates the 4 pipes needed - 2 for input from clients
		//and 2 for output to clients
		public void connectClients(ClientA a, ClientB b) {
			try {
				//From ClientA to server
				fromA = new PipedInputStream(a.getOutputStream());
				receiverA = new Receiver(fromA);
				//From ClientB to server
				fromB = new PipedInputStream(b.getOutputStream());
				receiverB = new Receiver(fromB);
				//From ClientA to ClientB
				toA = new PipedOutputStream(a.getInputStream());
				//From ClientB to ClientA
				toB = new PipedOutputStream(b.getInputStream());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		//Starts up the receivers to be ready to read anytime
		//Then starts up the server
		public void start() throws IOException {
			receiverA.start();
			receiverB.start();
			serverThread.start();
		}
		
		//Constantly checks to see if something has been read
		//If something has been read a vector of bytes will pop up
		//The bytes are sent to the client it was inteaded to go to
		@Override
		public void run() {
			while (running) {
				try {
					byteVec = receiverA.receiveBytes();
					if(byteVec != null) {
						bytes = new byte[byteVec.size()];
						
						for(int i = 0; i < bytes.length; i++) {
							bytes[i] = byteVec.elementAt(i).byteValue();
						}
						
						toB.write(bytes);
						toB.flush();
					}
					byteVec = receiverB.receiveBytes();
					if(byteVec != null) {
						bytes = new byte[byteVec.size()];
						
						for(int i = 0; i < bytes.length; i++) {
							bytes[i] = byteVec.elementAt(i).byteValue();
						}
						
						toA.write(bytes);
						toA.flush();
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
}