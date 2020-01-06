package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class Client implements Runnable{
	
	//Client Variables
	private String host;
	private int port;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in; 
	private boolean running = false;
	
	//Constructor
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	//Connect to the server 
	public void connect() {
		try {
			socket = new Socket(host,port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in =  new ObjectInputStream(socket.getInputStream());
			new Thread(this).start();
		}catch(ConnectException e) { //Incase not connected to server or Internet
			System.out.println("Sorry, unable to connect to server.");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
}
