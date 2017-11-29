package edu.ufl.cise.bit_torrent_components;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Handshake_Client {
	Socket requestSocket;           //socket connect to the server
	ObjectOutputStream out;         //stream write to the socket
 	ObjectInputStream in;          //stream read from the socket
	String message;                //message send to the server
	String MESSAGE;                //capitalized message read from the server
	DataOutputStream handshake;
	
    private final static String _protocolId = "P2PFILESHARINGPROJ";
    //char[] c = new char[18];
    char[] c = {'P', '2', 'p', 'f', 'i', 'l', 'e', 's', 'h', 'a', 'r', 'i', 'n', 'g', 'p', 'r', 'o', 'j'};
    String protid1 = "P2PFILESHARINGPROJ";
    String zeroBits1 = "0000000000";
    String peerId1 = "0001"; 
    String handshake1 = protid1+zeroBits1+peerId1;
    //private final byte[] _zeroBits = new byte[10];
    //private final byte[] _peerId = new byte[4];
    //private final byte[] pid = new byte[18];
    byte[] zeroBits = new byte[10];
    byte[] pid = new byte[18];
    byte[] peerId = new byte[4];
    //pid = _protocolId.getBytes();
    //ByteBuffer target = ByteBuffer.wrap(bigByteArray);
    //target.put(pid);
    //target.put(small2);
    //String string = new String(byte[] b);
    //String s = b.toString();
	public void Client() {}

	void run()

	{
		String part1, part2, part3;
		//byte[] one = getBytesForOne();
		//byte[] two = getBytesForTwo();
		//byte[] combined = new byte[zeroBits.length + pid.length + peerId.length];

		/*for (int i=0; i<c.length; i++) {
			pid[i] = (byte) c[i];
		}
		
		for (int i = 0, k=0, j=0; i < combined.length; ++i, k++, j++)
		{
			if (i<18)
		    	combined[i] = pid[i];
			else if (i<28)
				combined[i] = zeroBits[k];
			else
				combined[i] = peerId[j];
		}*/
		try{
			//create a socket to connect to the server
			requestSocket = new Socket("localhost", 8000);
			System.out.println("Connected to localhost in port 8000");
			//System.out.println(s);
			//initialize inputStream and outputStream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			//get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				System.out.print("Hello, please input a sentence: ");
				//read a sentence from the standard input
				message = bufferedReader.readLine();
				//Send the sentence to the server
				//sendMessage(message);
				sendMessage(handshake1);
				System.out.println(handshake1);
				//sendMessage(_protocolId);
				//sendMessage(b);
				//this._peerId = 1;
				
				//Receive the upperCase sentence from the server
				MESSAGE = (String)in.readObject();
				part1 = MESSAGE.substring(0, 18);
				part2 = MESSAGE.substring(18, 28);
				part3 = MESSAGE.substring(28, 32);
				//if ((part1 == protid1) && (part2 == zeroBits1)){
				if(part1.equals(protid1) && part2.equals(zeroBits1)) {
					System.out.println("Connecting to peer" + part3);	
				}
				else
				{
					System.out.println("could not pair with" + part3);
				}
				//show the message to the user
				//System.out.println("Receive message: " + MESSAGE);
			}
		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		catch ( ClassNotFoundException e ) {
            		System.err.println("Class not found");
        	} 
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	//send a message to the output stream
	void sendMessage(String msg)
	{
		try{
			//stream write the message
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	//main method
	public static void main(String args[])
	{
		Handshake_Client client = new Handshake_Client();
		client.run();
	}

}
