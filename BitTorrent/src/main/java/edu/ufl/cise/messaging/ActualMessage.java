package edu.ufl.cise.messaging;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import edu.ufl.cise.bit_torrent_components.*;
import edu.ufl.cise.logging.Logfile;
import edu.ufl.cise.p2p.FileHandler;
import edu.ufl.cise.p2p.Peer;
import edu.ufl.cise.p2p.PeerHandler;
import edu.ufl.cise.p2p.message.Bitfield;
import edu.ufl.cise.p2p.message.Handshake;
import edu.ufl.cise.p2p.message.Have;
import edu.ufl.cise.p2p.message.Interested;
import edu.ufl.cise.p2p.message.Message;
import edu.ufl.cise.p2p.message.NotInterested;
import edu.ufl.cise.p2p.message.Piece;
import edu.ufl.cise.p2p.message.Request;
import edu.ufl.cise.p2p.message.Terminate;


public class ActualMessage implements Serializable
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   byte[] message_length=new byte[4];
   byte message_type;
   byte[] message_payload;
   int messageLength;
   Logfile log;
   public ActualMessage(int type,byte[] payload)
   {
	   message_type = (byte) type;
	   this.message_payload = payload;
	   if(payload==null)
		   messageLength=1;                        //includes type as well
	   else
	   {
		   messageLength=payload.length+1;
	   }
	   message_length = ByteBuffer.allocate(4).putInt(messageLength).array();
   }
   public void writeBytesForHandshake(ObjectOutputStream obj) throws IOException
   {
	   obj.write(message_length,0,message_length.length);
	   obj.writeByte(message_type);
	   obj.write(message_payload, 0, message_payload.length);
   }
   public void readBytesForHandshake(ObjectInputStream obj) throws IOException
   {
	   obj.read(message_length,0,message_length.length);
	   obj.readByte();
	   obj.read(message_payload,0,messageLength-1);
   }
}

class MessageResponse {

	boolean isChokedByRemotePeer;
	FileHandler fileHandler;
	List<RemotePeer> remotePeers;
	edu.ufl.cise.bit_torrent_components.Peer locaPeer;
	Logfile log;
	PeerHandler peerHandler;

	public MessageResponse(FileHandler fileHandler,
			List<RemotePeer> remotePeers, Peer localPeer,
			PeerHandler peerHandler) throws IOException {
		isChokedByRemotePeer = true;
		this.fileHandler = fileHandler;
		this.remotePeers = remotePeers;
		this.locaPeer = localPeer;
		this.log = new Logfile(localPeer.getId());
		this.peerHandler = peerHandler;
	}

	public ActualMessage createResponse(Handshaking_Message handshake) {
		BitSet bitSet = fileHandler.getBitSet();
		ActualMessage message=null;
		if(!bitSet.isEmpty())
		{
			message=new BitField(bitSet);
		}
		return message;
	}

	public ActualMessage createResponse(ActualMessage message, RemotePeer remotePeer) {
		//int type = message.message_type;
		switch ((int)message.message_type) {
		case 0:
			log.choking(remotePeer.peer_id);
			System.out.println("Received CHOKE from peer :" + remotePeer.peer_id);
			isChokedByRemotePeer = true;
			fileHandler.getNeededPieces().addAll(remotePeer.getRequestedPieces());
			remotePeer.getRequestedPieces().clear();
			break;
			
		case 1:
			log.unchoking(remotePeer.peer_id);
			System.out.println("Received UNCHOKE from peer :"+ remotePeer.peer_id);
			isChokedByRemotePeer = false;
			return getRequestMessage(remotePeer);

		case 2:
			log.receivedInterestedMessage(remotePeer.peer_id);
			System.out.println("Received INTERESTED from peer :"+ remotePeer.peer_id);
			System.out.println("Marking [" + remotePeer.peer_id	+ "] as interested");
			remotePeer.isInterested.set(true);
			break;
		case 3:
			log.receivedNotInterestedMessage(remotePeer.peer_id);
			System.out.println("Received NOT INTERESTED from peer :"+ remotePeer.peer_id);
			System.out.println("Marking [" + remotePeer.peer_id+ "] as NOT interested");
			remotePeer.isInterested.set(false);
			break;
		case 4:
			System.out.println("Received HAVE from peer :" + remotePeer.peer_id);
			Have have = (Have) message;
			int index = have.getPayloadasIndex();
			log.receivedHaveMessage(remotePeer.peer_id, have.getPayloadasIndex());
			remotePeer.getBitset().set(index);
			if (fileHandler.getBitSet().get(index))
			{
				System.out.println("Peer already has part of index: " + index+ ". Sending NOT interested");
				return new NotInterested();
			} 
			else
			{
				System.out.println("Peer does not have part of index: " + index+ ". Sending interested");
				return new Interested();
			}
		case 5:
			BitField bitfield = (BitField) message;
			BitSet bitSet = bitfield.getBitSet_for_BitField();
			System.out.println("BitSet of size :" + bitSet.size()+ " received from peer :" + remotePeer.peer_id);
			remotePeer.setBitset(bitSet);
			bitSet.andNot(fileHandler.getBitSet());
			if (!bitSet.isEmpty()) 
			{
				return new Interested();
			}
			else
			{
				return new NotInterested();
			}
		case 6:
			Request request = (Request) message;
			int indexOfPieceRequested = request.getRequestMessage();
			System.out.println("Index :" + indexOfPieceRequested+ " requested by peer [" + remotePeer.peer_id + "]");
			byte[] data = fileHandler.getDataFromPiece(indexOfPieceRequested);
			return new Piece(indexOfPieceRequested, data);
		case 7:
			Piece piece = (Piece) message;
			int pieceIndex = piece.getIndex();
			remotePeer.getRequestedPieces().remove(pieceIndex);
			fileHandler.getRequestedPieces().decrementAndGet();
			System.out.println("Piece of index [" + piece.getIndex()+ "] received from peer [" + remotePeer.peer_id + "]");
	
			fileHandler.writePieceData(pieceIndex, piece.getContent());
			fileHandler.getBitSet().set(pieceIndex);
			remotePeer.getBytesDownloaded().getAndAdd(piece.getContent().length);
			int totalPiecesDownloaded = fileHandler.getBitSetLength()
					- fileHandler.getNeededPieces().size();
			for (RemotePeer rPeer : remotePeers) 
			{
				if (rPeer.getConnection() == null)
					continue;
				if (!rPeer.getBitSet().get(pieceIndex)) {
					System.out.println("Sending HAVE Message to ["	+ rPeer.peer_id + "]");
					rPeer.getConnection().sendMessage(new Have(pieceIndex));
				}
				totalPiecesDownloaded -= rPeer.getRequestedPieces().size();
			}
			log.downloadingPiece(remotePeer.peer_id, pieceIndex,
					totalPiecesDownloaded);

			if (fileHandler.getBitSet().cardinality() == fileHandler.getBitSetLength()) 
			{
				fileHandler.mergeFilesInto(fileHandler.getBitSetLength());
				log.logCompletion();
				fileHandler.getIsComplete().getAndSet(true);
				for(RemotePeer remotePeer : remotePeers){
					remotePeer.getConnection().sendMessage(new Terminate(8));
				}
				checkTermination();
			}
			if (!isChokedByRemotePeer)
				return getRequestMessage(remotePeer);

		case 8:
			remotePeer.getIsTerminated().getAndSet(true);
			checkTermination();
		}
		return null;
	}

	private Message getRequestMessage(RemotePeer rPeer) {
		fileHandler.getRequestedPieces().incrementAndGet();
		BitSet copy = (BitSet) rPeer.getBitSet().clone();
		copy.andNot(fileHandler.getBitSet());
		List<Integer> reqPieceIndices = new ArrayList<Integer>();
		for (int i = copy.nextSetBit(0); i >= 0
				&& fileHandler.getNeededPieces().contains(i); i = copy
				.nextSetBit(i + 1)) {
			reqPieceIndices.add(i);
		}
		if (reqPieceIndices.isEmpty()) {
			locaPeer.getHasFile().set(true);
			return null;
		}

		Random r = new Random();
		int randomListIndex = r.nextInt(reqPieceIndices.size());
		System.out.println("Requesting piece index :"
				+ reqPieceIndices.get(randomListIndex));
		int pieceIndex = reqPieceIndices.get(randomListIndex);
		fileHandler.getNeededPieces().remove(pieceIndex);
		rPeer.getRequestedPieces().add(pieceIndex);
		return new Request(pieceIndex);
	}

	private boolean checkTermination() {
		for (RemotePeer remote : remotePeers) {
			if (!remote.getIsTerminated().get())
				return false;
		}
		if (!fileHandler.getIsComplete().get())
			return false;
		// Call something that terminates everything
		peerHandler.stopChokeAndUnchokeMessages();
		for (RemotePeer remote : remotePeers) {
			if (remote.getConnection() != null)
				remote.getConnection().getTerminate().set(true);
		}
		locaPeer.getTerminate().set(true);
		return true;
	}

}