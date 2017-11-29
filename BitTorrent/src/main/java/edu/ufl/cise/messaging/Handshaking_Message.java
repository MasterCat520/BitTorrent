package edu.ufl.cise.messaging;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class Handshaking_Message implements Externalizable
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   String handshake_header= "P2PFILESHARINGPROJ";
   byte[] header_handshake = new byte[18] ;
   byte[] zero_bits;
   byte[] peer_ID=new byte[4];
   
   public Handshaking_Message(int peer_ID) 
   {
       this.header_handshake=handshake_header.getBytes();
	   this.zero_bits = new byte[10];
	   this.peer_ID = ByteBuffer.allocate(4).putInt(peer_ID).array();
   }

@Override
public void readExternal(ObjectInput obj) {
	// TODO Auto-generated method stub
	try
	{
	   if(handshake_header.getBytes().length>=header_handshake.length)
	   {
	      obj.read(header_handshake,0,header_handshake.length);
	   }
	   else
	   {
		   throw new Exception("Header length for handshake is not equal to 18 bytes");
	   }
	   obj.read(zero_bits, 0, zero_bits.length);
	   if(peer_ID.length>=4)
	   {
	      obj.read(peer_ID, 0, peer_ID.length);
	   }
	   else
	   {
		   throw new Exception("Peer ID length for handshake is not equal to 4 bytes");
	   }
	}
	catch (Exception e)
	{
		System.out.println("Exception in either handshake header or peer ID");
	}
}

@Override
public void writeExternal(ObjectOutput obj) throws IOException {
	// TODO Auto-generated method stub
	obj.write(header_handshake,0,header_handshake.length);
	obj.write(zero_bits, 0, zero_bits.length);
	obj.write(peer_ID, 0, peer_ID.length);
}
   
   
}
