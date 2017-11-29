package edu.ufl.cise.logging;

import java.util.ArrayList;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import edu.ufl.cise.bit_torrent_components.RemotePeer;


public class Logfile 
{
   private static final Logger logger=Logger.getLogger(Logfile.class);
   private String peer_id;
   public Logfile(String peer_id) throws Exception
   {
	   this.peer_id=peer_id;
	   SimpleLayout layout=new SimpleLayout();
	   FileAppender appender=new FileAppender(layout,"log_peer_"+this.peer_id+".log",false);
	   logger.addAppender(appender);
	   logger.setLevel((Level)Level.INFO);
   }
   
   /**
    *   Logs the peer ID of the connection
    */
   
   private void peer_connection()
   {
	   if(logger.isInfoEnabled())
	   {
		   logger.info("Connected to peer ID:"+this.peer_id);
	   }
   }
   
   /**
    * Logs TCP connection made to other peer
    * @param main_peer_id 
    * @param remote_peer_id
    */
   
   public void makeTCPConnection(String main_peer_id, String other_peer_id)
   {
	   if(logger.isInfoEnabled())
	   {
		   logger.info(main_peer_id+" able to make connection Connected to peer ID:" +other_peer_id);
	   }
	   else
	   {
		   logger.info("Connection unsuccessful");
	   }
   }
   
   /**
    *  logs the preferred neighbours of main peer
    * @param preferredNeighboursList provides a list of all connected remote peers
    */
   
   public void changePreferredNeighbors(ArrayList<RemotePeer> preferredNeighboursList)
	{
		StringBuffer event = new StringBuffer();
		event.append(this.peer_id + " has the preferred neighbors ");
		
		for(int i = 0; i < (preferredNeighboursList.size() - 1); i++)
		{
			event.append(preferredNeighboursList.get(i).peer_id + ", ");
		}

		event.append(preferredNeighboursList.get(preferredNeighboursList.size() - 1).peer_id + "\n");
		if(logger.isInfoEnabled())
		{
			logger.info(event.toString());
		}
	}
   
    /**
     * peer changes  its  optimistically  unchoked  neighbor
     * @param optimisticallyUnchokedNeighbour
     */
   
    public void changeOptimisticallyUnchokedNeighbor(String optimisticallyUnchokedNeighbour)
    {
    	if(logger.isInfoEnabled())
		{
			logger.info(this.peer_id+"has optimistically unchocked neighbour "+optimisticallyUnchokedNeighbour+"\n");
		}
    }
    
    /**
     * peer is unchoked by other remote peer
     * @param remotePeer
     */
    
    public void unchoking(String remotePeer)
    {
    	if(logger.isInfoEnabled())
		{
			logger.info(this.peer_id+" is unchocked by "+remotePeer+"\n");
		}
    }
    
    /**
     * peer is choked by other remote peer
     * @param remotePeer
     */
    
    public void choking(String remotePeer)
    {
    	if(logger.isInfoEnabled())
		{
			logger.info(this.peer_id+" is chocked by "+remotePeer+"\n");
		}
    }
    
    /**
     * peer received the 'Have' message
     * @param remotePeer
     */
    
    public void receivedHaveMessage(String remotePeer,int piece_index)
    {
    	if(logger.isInfoEnabled())
		{
			logger.info(this.peer_id+" received the ‘have’ message from "+remotePeer+ " for piece index: "+piece_index+"\n");
		}
    }
    
    /**
     * peer received the 'Interested' message
     * @param remotePeer
     */
    
    public void receivedInterestedMessage(String remotePeer)
    {
    	if(logger.isInfoEnabled())
		{
			logger.info(this.peer_id+" received the ‘interested’ message from "+remotePeer+"\n");
		}
    }
    
    /**
     * peer received the 'Not Interested' message
     * @param remotePeer
     */
    
    public void receivedNotInterestedMessage(String remotePeer)
    {
    	if(logger.isInfoEnabled())
		{
			logger.info(this.peer_id+" received the ‘not interested’ message from "+remotePeer+"\n");
		}
    }
    
    /**
     * peer downloads the piece from other pair
     * @param remotePeer
     * @param piece_index the start index of downloaded piece
     * @param num_pieces total number of downloaded pieces
     */
    
    public void downloadingPiece(String remotePeer,int piece_index,int num_pieces)
    {
    	if(logger.isInfoEnabled())
		{
			logger.info(this.peer_id+" has downloaded the piece "+piece_index+" from "+remotePeer+
					".\nNow the number of pieces it has is "+num_pieces);
		}
    }
    
    /**
     * peer completes downloading of file
     */
    
    public void downloadCompletion()
    {
    	if(logger.isInfoEnabled())
		{
			logger.info(this.peer_id+" has downloaded the complete file\n");
		}
    }
}
