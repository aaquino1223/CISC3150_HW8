import java.io.*;

//Alex Aquino
//Homework 8
//Sets everything up by starting by connecting
//server and clients via connectClients method
//Starts them all up
public class HW8_Main {
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		ClientA clientA = new ClientA();
		ClientB clientB = new ClientB();
		server.connectClients(clientA, clientB);
		
		try {
			server.start();
			clientA.start();
			clientB.start();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}