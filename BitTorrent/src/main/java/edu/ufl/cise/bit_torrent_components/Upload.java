package edu.ufl.cise.bit_torrent_components;
//package p2p;

//DataOutputStream
import java.net.*;
import java.io.*;

public class Upload implements Runnable {
	private Thread t;
	private String threadName;
	private boolean finish;

	DataOutputStream out;

	Upload(DataOutputStream x, String name, boolean value) {
		out = x;
		threadName = name;
		finish = value;
	}

	public void run() {
		try {
			//String x = "upload " + System.console().readLine();
			out.writeUTF("hello from " + threadName);
			if(finish)
				out.writeUTF("finish");
			//out.writeUTF("upload hello" + System.console().readLine());
			//out.writeUTF("Hello from upload");
			// System.out.println("Socket timed out!");
		} catch (IOException e) {
			System.out.println(threadName + " upload Socket timed out!");
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
