package edu.ufl.cise.bit_torrent_components;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Handshake_Server {

	private static final int sPort = 8000;   //The server will be listening on this port number

	public static void main(String[] args) throws Exception {
		System.out.println("The server is running."); 
        	ServerSocket listener = new ServerSocket(sPort);
		int clientNum = 1;
        	try {
            		while(true) {
                		new Handler(listener.accept(),clientNum).start();
				System.out.println("Client "  + clientNum + " is connected!");
				clientNum++;
            			}
        	} finally {
            		listener.close();
        	} 
 
    	}

	/**
     	* A handler thread class.  Handlers are spawned from the listening
     	* loop and are responsible for dealing with a single client's requests.
     	*/
    	private static class Handler extends Thread {
        	private String message;    //message received from the client
		private String MESSAGE;    //uppercase message send to the client
		private Socket connection;
        	private ObjectInputStream in;	//stream read from the socket
        	private ObjectOutputStream out;    //stream write to the socket
		private int no;		//The index number of the client
		
	    String protid1 = "P2PFILESHARINGPROJ";
	    String zeroBits1 = "0000000000";
	    String peerId1 = "0002"; 
	    String handshake1 = protid1+zeroBits1+peerId1;

        	public Handler(Socket connection, int no) {
            		this.connection = connection;
	    		this.no = no;
        	}

        public void run() {
        	String part1, part2, part3;
 		try{
			//initialize Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			try{
				while(true)
				{
					//receive the message sent from the client
					message = (String)in.readObject();
					//show the message to the user
					System.out.println("Receive message: " + message + " from client " + no);
					//Capitalize all letters in the message
					//MESSAGE = message.toUpperCase();
					//read handshake
					part1 = message.substring(0, 18);
					part2 = message.substring(18, 28);
					part3 = message.substring(28, 32);
					//if ((part1 == protid1) && (part2 == zeroBits1)){
					if(part1.equals(protid1) && part2.equals(zeroBits1)) {
						System.out.println("Connecting to peer" + part3);
						
					}
					else
					{
						System.out.println("could not pair with" + part3);
					}
					//System.out.println(part1);
					//System.out.println(protid1);
					//System.out.println(part2);
					//System.out.println(zeroBits1);
					//System.out.println(part3);
					//send MESSAGE back to the client
					//send handshake
					sendMessage(handshake1);
				}
			}
			catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}
		}
		catch(IOException ioException){
			System.out.println("Disconnect with Client " + no);
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				connection.close();
			}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + no);
			}
		}
	}

	//send a message to the output stream
	public void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("Send handshake message: " + msg + " to Client " + no);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

    }

}
