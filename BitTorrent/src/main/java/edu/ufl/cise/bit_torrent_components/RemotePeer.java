package edu.ufl.cise.bit_torrent_components;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * This class encompasses all the characterstics of RemotePeer
 *
 */
public class RemotePeer 
{
   public String ip_address;
   public int port_no;
   public String peer_id;
   public boolean hasFile;
   public AtomicBoolean isPreferredNeighbour;
   public AtomicBoolean isOptimisticallyUnchockedNeighbour;
   public AtomicBoolean isUnchoked;
   public AtomicBoolean isChoked;
   public AtomicBoolean isInterested;
   public AtomicInteger bytes_downloaded;
   //public List<Integer> pieces_to_download;
   public AtomicBoolean has_finished_downloading;
   private BitSet bitset;
   
   public RemotePeer(String ip_address, int port_no, String peer_id, boolean hasFile)
   {	
	   this.ip_address = ip_address;
	   this.port_no = port_no;
	   this.peer_id = peer_id;
	   this.hasFile = hasFile;
	   this.isPreferredNeighbour = new AtomicBoolean(false);
	   this.isOptimisticallyUnchockedNeighbour = new AtomicBoolean(false);
	   this.isUnchoked = new AtomicBoolean(false);
	   this.isChoked = new AtomicBoolean(false);
	   this.isInterested = new AtomicBoolean(false);
	   this.bytes_downloaded=new AtomicInteger(0);
	   //this.pieces_to_download = new ArrayList<Integer>();
	   this.has_finished_downloading = new AtomicBoolean(false);
   }

public BitSet getBitset() {
	return bitset;
}

public void setBitset(BitSet bit_set) {
	this.bitset = bit_set;
}
   
}
