package cs555.crawler.node;

import cs555.crawler.communications.Link;
import cs555.crawler.peer.PeerList;
import cs555.crawler.url.CrawlerState;
import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;

public class NodeManager extends Node{
	
	CrawlerState state;
	PeerList peerList;
	
	String linkFile;
	String slaveFile;
	int maxDepth;
	
	//================================================================================
	// Constructor
	//================================================================================
	public NodeManager(CrawlerState s, PeerList list, int port,String lf, String sf, int md){
		super(port);
		
		peerList = list;
		state = s;
		linkFile = lf;
		slaveFile = sf;
		maxDepth = md;
		
	}
	
	
	
	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);
		
		switch (messageType) {
		case Constants.Payload:
			
			break;

		default:
			
			System.out.println("Unrecognized Message");
			break;
		}
	}
	
	
	//================================================================================
	//================================================================================
	// Main
	//================================================================================
	//================================================================================
	public static void main(String[] args){

		int port = 0;
		int maxDepth = 5;
		String linkFile = "";
		String slaveFile = "";

		if (args.length == 3) {
			port = Integer.parseInt(args[0]);
			linkFile = args[1];
			slaveFile = args[2];
		}

		else if (args.length == 4){
			port = Integer.parseInt(args[0]);
			linkFile = args[1];
			slaveFile = args[2];
			maxDepth = Integer.parseInt(args[3]);
		}

		else {
			System.out.println("Usage: java node.NodeManager PORT LINK-FILE SLAVE-FILE <DEPTH>");
			System.exit(1);
		}

		// Create peer list
		PeerList peerList = new PeerList(slaveFile, port);
		CrawlerState state = new CrawlerState(linkFile);

		// Create node
		NodeManager manager = new  NodeManager(state, peerList, port, linkFile, slaveFile, maxDepth);
		manager.initServer();
		
	}
}
