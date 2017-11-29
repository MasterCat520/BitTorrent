package edu.ufl.cise.messaging;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Piece extends ActualMessage{

	public Piece(int index, byte[] piece_content) 
	{
		super(7, getActualPayload(index,piece_content));
		// TODO Auto-generated constructor stub
	}

	private static byte[] getActualPayload(int index, byte[] piece_content) {
		// TODO Auto-generated method stub
		byte[] actual_payload;
		byte[] piece_index=ByteBuffer.allocate(4).putInt(index).array();
		if(piece_content==null)
		{
			actual_payload=new byte[4];
		}
		else
		{
			actual_payload=new byte[4+piece_content.length];
		}
		System.arraycopy(piece_index, 0, actual_payload, 0, 4);
		System.arraycopy(piece_content, 0, actual_payload, 4, piece_content.length);
		return actual_payload;
	}
	
	public byte[] getContent() {
		return Arrays.copyOfRange(message_payload, 4, message_payload.length);
	}
    
	public int getIndex() {
		return ByteBuffer.wrap(Arrays.copyOfRange(message_payload, 0, 4)).getInt();
	}
}
