package edu.ufl.cise.bit_torrent_components;
//NumberOfPreferredNeighbors 2
//UnchokingInterval 5
//OptimisticUnchokingInterval 15
//FileName TheFile.dat
//FileSize 10000232
//PieceSize 32768
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inputread {
	public static void main(String[] args) {
		int numberOfPreferredNeighbors = -1, unchokingInterval = -1, optimisticUnchokingInterval = -1;
		String fileName = "~";
		int fileSize = -1, pieceSize = -1;
		int peerid, port, file;
		String ipaddr;
		BufferedReader br;
		Map<Integer, List<Object>> peerinfo_map=new HashMap<>();
		List<Object> peerinfo_list=new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader("Common.cfg"));
			String line = br.readLine();
			while (line != null) {
				System.out.println(line);
				String[] parts = line.split(" ");
				switch (parts[0]) {
				case "NumberOfPreferredNeighbors":
					numberOfPreferredNeighbors = Integer.parseInt(parts[1]);
					break;
				case "UnchokingInterval":
					unchokingInterval = Integer.parseInt(parts[1]);
					break;
				case "OptimisticUnchokingInterval":
					optimisticUnchokingInterval = Integer.parseInt(parts[1]);
					break;
				case "FileName":
					fileName = parts[1];
					break;
				case "FileSize":
					fileSize = Integer.parseInt(parts[1]);
					break;
				case "PieceSize":
					pieceSize = Integer.parseInt(parts[1]);
					break;
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.exit(0);
			e.printStackTrace();
		}
		System.out.println("printing");
		System.out.println(numberOfPreferredNeighbors + " " + unchokingInterval + " " + optimisticUnchokingInterval);
		System.out.println(fileName + " " + fileSize + " " + pieceSize);
		// 1001 lin114-00.cise.ufl.edu 6008 1
		try {
			br = new BufferedReader(new FileReader("PeerInfo.cfg"));
			String line = br.readLine();
			while (line != null) {
				String[] parts = line.split(" ");
				peerid = Integer.parseInt(parts[0]);
				ipaddr = parts[1];
				port = Integer.parseInt(parts[2]);
				file = Integer.parseInt(parts[3]);
				peerinfo_list.add(ipaddr);
				peerinfo_list.add(port);
				peerinfo_list.add(file);
				peerinfo_map.put(peerid, peerinfo_list);
				System.out.println(peerid + " " + ipaddr + " " + port + " " + file);
				peerinfo_list=new ArrayList<>();
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.exit(0);
			e.printStackTrace();
		}

	}
}