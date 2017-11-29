package edu.ufl.cise.bit_torrent_components;
//package p2p;
import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
public class Peerserver extends Thread {
	public static AtomicBoolean finish = new AtomicBoolean();
	private ServerSocket serverSocket;
	private String threadName;
	
	public Peerserver(int port, String name, boolean x) throws IOException {
		serverSocket = new ServerSocket(port);
		threadName = name;
		finish.set(x);
		serverSocket.setSoTimeout(1000);
	}

	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();

				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(server.getInputStream());

				Download d = new Download(in, threadName);
				d.start();

				// System.out.println(in.readUTF());
				DataOutputStream out = new DataOutputStream(server.getOutputStream());

				Upload u = new Upload(out, threadName, false);
				u.start();

			} catch (SocketTimeoutException s) {
				
				if(finish.get()) {
					
					System.out.println("finish = " + finish.toString() + " Socket timed out!");
					break;
				}
				else
					System.out.println("printing else");
			
				//break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}

	}
}
