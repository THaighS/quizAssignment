package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Runnable{
	
	//Client Variables
	private String host;
	private int port;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in; 
	private boolean running = false;
	private EventListener listener;
	
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
			listener = new EventListener();
			new Thread(this).start();
		}catch(ConnectException e) { //In case not connected to server or Internet
			System.out.println("Sorry, unable to connect to server.");
		}catch(IOException e) { //error message 
			e.printStackTrace();
		}
	}
	
	//close connection - if player logs off (server off, input/output/serv.sock close)
	public void close() {
		try {
			running = false;
			RemovePlayerPacket packet = RemovePlayerPacket(); //tell sever that we disconnected 
			sendObject(packet); //send it before server disconnects 
			in.close();
			out.close();
			socket.close();
		}catch(IOException e) { //error message 
			e.printStackTrace();
		}
	}
	
	//send data to server
	public void sendObject(Object packet) {
		try {
			out.writeObject(packet);
		}catch(IOException e) { //error message 
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() { // start going 
		try {
			running = true;
			
			while(running) {
				try { //listen for new data 
					Object data = in.readObject();//read and listen for new object(data)
					listener.received(data); //handle data
					
				}catch(ClassNotFoundException e) {//unable to get/find object or don't have copy on client
					e.printStackTrace();
				}catch(SocketException e) { //problem with socket connection while client running 
					close(); //nothing is going to work, stop other client experiencing problems 
				}
			
			}
		}catch(IOException e) { //Catch allll there errorsss
			e.printStackTrace();
		}
		
	}

	
}
