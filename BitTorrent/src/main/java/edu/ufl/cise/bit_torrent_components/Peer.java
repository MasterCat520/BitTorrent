package edu.ufl.cise.bit_torrent_components;
//package p2p;
import java.net.*;
import java.io.*;
import java.lang.*;

public class Peer {
	static String serverName = "localhost";
	static String processname;

	public static void connect(int port, boolean finish) {
		try {
			System.out.println("Connecting to " + serverName + " on port " + port);
			Socket client = new Socket(serverName, port);

			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();

			DataOutputStream out = new DataOutputStream(outToServer);
			Upload u = new Upload(out, processname, finish);
			u.start();

			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			Download d = new Download(in, processname);
			d.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		
		int id = Integer.parseInt(args[0]);
		processname = "peer" + args[0];
		// int port = Integer.parseInt(args[2]);
		int serverPort = 2222;

		try {
			switch (id) {
			case 0:
				serverPort = 2222;
				break;
			case 1:
				serverPort = 3333;
				connect(2222, false);
				break;
			case 2:
				serverPort = 4444;
				connect(2222, false);
				connect(3333, false);
				break;
			case 3:
				serverPort = 5555;
				connect(2222, true);
				connect(3333, true);
				connect(4444, true);
				break;

			}

			if (id == 3) {
				Thread t = new Peerserver(serverPort, processname, true);
				t.start();
			} else {
				Thread t = new Peerserver(serverPort, processname, false);
				t.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
