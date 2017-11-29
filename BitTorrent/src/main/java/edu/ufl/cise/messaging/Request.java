package edu.ufl.cise.messaging;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class Request extends ActualMessage {

	public Request(int index) {
		super(6, ByteBuffer.allocate(4).putInt(index).array());
		// TODO Auto-generated constructor stub
	}
    
	public int getRequestMessage()
	{
		return ByteBuffer.wrap(message_payload).getInt();
	}

}
