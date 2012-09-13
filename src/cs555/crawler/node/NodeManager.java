package cs555.crawler.node;

import cs555.crawler.communications.Link;
import cs555.crawler.peer.Peer;
import cs555.crawler.peer.PeerList;
import cs555.crawler.url.CrawlerState;
import cs555.crawler.url.Page;
import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;
import cs555.crawler.wireformats.ElectionMessage;
import cs555.crawler.wireformats.FetchRequest;

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
	// Round
	//================================================================================
	public boolean shouldContinue(){
		return state.shouldContinue();
	}
	
	public void beginRound(){
		// Get Peer
		Peer peer = peerList.getNextPeer();
		Link link = connect(peer);
		
		// Get links
		Page page = state.getNextReadyPage();
		FetchRequest request = page.getFetchRequest();
		link.sendData(request.marshall());
		
		System.out.println("Sent: \n" + request);
	}
	
	//================================================================================
	// Send
	//================================================================================
	public void broadcastElection(){
		ElectionMessage electionMsg = new ElectionMessage(Constants.Election_Message);
		broadcastMessage(peerList.getAllPeers(), electionMsg.marshall(),Constants.Election_Message);
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
		CrawlerState state = new CrawlerState(linkFile,maxDepth);

		// Create node
		NodeManager manager = new  NodeManager(state, peerList, port, linkFile, slaveFile, maxDepth);
		manager.initServer();
		
		// Broadcast our election message
		manager.broadcastElection();
		
		// If we should continue, continue
		while (manager.shouldContinue()){
			manager.beginRound();
		}
	}
}
