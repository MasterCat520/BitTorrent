package edu.ufl.cise.messaging;

import java.util.BitSet;

public class BitField extends ActualMessage {

	public BitField(byte[] payload) {
		super(5, payload);
		// TODO Auto-generated constructor stub
	}
	
	public BitField(BitSet bitSet) {
		super(5, bitSet.toByteArray());
	}
	
	public BitSet getBitSet_for_BitField()
	{
		return BitSet.valueOf(message_payload);
	}
}
