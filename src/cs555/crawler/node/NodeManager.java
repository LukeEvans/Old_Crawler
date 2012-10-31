package cs555.crawler.node;

import java.util.ArrayList;

import cs555.crawler.communications.Link;
import cs555.crawler.peer.Peer;
import cs555.crawler.peer.PeerList;
import cs555.crawler.url.CrawlerState;
import cs555.crawler.url.Page;
import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;
import cs555.crawler.wireformats.ElectionMessage;
import cs555.crawler.wireformats.FetchRequest;
import cs555.crawler.wireformats.FetchResponse;
import cs555.crawler.wireformats.HandoffLookup;

public class NodeManager extends Node{
	
	CrawlerState state;
	PeerList peerList;
	
	String linkFile;
	String slaveFile;
	int maxDepth;
	
	//================================================================================
	// Constructor
	//================================================================================
	public NodeManager(CrawlerState s, PeerList list, int port,String lf, String sf){
		super(port);
		
		peerList = list;
		state = s;
		linkFile = lf;
		slaveFile = sf;
		maxDepth = Constants.depth;
		
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
		
		for (Page page : state.getAllPages()) {
			Peer peer = peerList.getReadyPeer();
			peer.setDomain(page.domain);
			
			ElectionMessage electionMsg = new ElectionMessage(serverPort, Tools.getLocalHostname(), page.domain, page.urlString);
			sendBytes(peer, electionMsg.marshall());
		}
	}

	
	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);
		
		switch (messageType) {
		case Constants.Fetch_Response:
			
			FetchResponse response = new FetchResponse();
			response.unmarshall(bytes);
			
			System.out.println("Got: " + response);
			
			break;

		case Constants.Handoff_Lookup:
			
			HandoffLookup lookup = new HandoffLookup();
			lookup.unmarshall(bytes);
			
			Peer leader = peerList.findDomainLeader(lookup.url);
			
			if (leader != null) {
				FetchRequest handoff = new FetchRequest(leader.domain, lookup.depth, lookup.url, new ArrayList<String>());
				sendBytes(leader, handoff.marshall());
			}
			
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
		String linkFile = "";
		String slaveFile = "";

		if (args.length == 3) {
			port = Integer.parseInt(args[0]);
			linkFile = args[1];
			slaveFile = args[2];
		}


		else {
			System.out.println("Usage: java node.NodeManager PORT LINK-FILE SLAVE-FILE");
			System.exit(1);
		}

		// Create peer list
		PeerList peerList = new PeerList(slaveFile, port);
		CrawlerState state = new CrawlerState(linkFile);

		// Create node
		NodeManager manager = new  NodeManager(state, peerList, port, linkFile, slaveFile);
		manager.initServer();
		
		// Broadcast our election message
		manager.broadcastElection();
		
	}
}
