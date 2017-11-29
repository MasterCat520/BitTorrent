//package p2p;

//DataInputStream

package edu.ufl.cise.bit_torrent_components;
import java.net.*;
import java.io.*;

public class Download implements Runnable {
	private Thread t;
	private String threadName;

	DataInputStream in;

	Download(DataInputStream x, String name) {
		in = x;
		threadName = name;
	}

	public void run() {
		try {
			System.out.println(threadName + " Download: Server says =" + in.readUTF());
			String s = "finish";
			if(s.equals(in.readUTF())) {
				System.exit(0);
				Peerserver.finish.set(true);
			}
			
		} catch (IOException e) {
			System.out.println(threadName + " download Socket timed out!");
		}

	}

	public void start() {		
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}
	
	public void join() {
		try {
			t.join();
		} catch (InterruptedException ie) {
			System.out.println("upload join interrupted");
		}
	}
}
