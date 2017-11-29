package edu.ufl.cise.messaging;

import java.nio.ByteBuffer;

public class Have extends ActualMessage{
    
	public Have(int index) {
		super(4, ByteBuffer.allocate(4).putInt(index).array());
		// TODO Auto-generated constructor stub
	}
    
	public int getPayloadasIndex()
	{
		return ByteBuffer.wrap(message_payload).getInt();
	}
}
