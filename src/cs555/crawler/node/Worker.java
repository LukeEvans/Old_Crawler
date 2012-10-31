package cs555.crawler.node;

import cs555.crawler.communications.Link;
import cs555.crawler.url.CrawlerState;
import cs555.crawler.url.Page;
import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;
import cs555.crawler.wireformats.ElectionMessage;
import cs555.crawler.wireformats.FetchRequest;
import cs555.crawler.wireformats.Verification;
import cs555.crawler.peer.Peer;
import cs555.crawler.pool.*;

public class Worker extends Node{

	Peer nodeManager;
	Link managerLink;
	ThreadPoolManager poolManager;
	String domain;
	CrawlerState state;

	//================================================================================
	// Constructor
	//================================================================================
	public Worker(int port,int threads){
		super(port);
		nodeManager = null;
		managerLink = null;
		poolManager = new ThreadPoolManager(threads);
		domain = new String();
		state = new CrawlerState();
	}


	public void initServer(){
		super.initServer();
		poolManager.start();
	}
	
	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);

		switch (messageType) {
		case Constants.Election_Message:
			ElectionMessage election = new ElectionMessage();
			election.unmarshall(bytes);
			
			Verification electionReply = new Verification(election.type);
			l.sendData(electionReply.marshall());
			
			nodeManager = new Peer(election.host, election.port);
			managerLink = connect(nodeManager);
			domain = election.domain;
			
			System.out.println("Elected Official: " + election);
			
			break;

		case Constants.Fetch_Request:
			FetchRequest request = new FetchRequest();
			request.unmarshall(bytes);
			
			System.out.println("Got: \n" + request);
			
			FetchParseTask task = new FetchParseTask(managerLink, request.url, request);
			poolManager.execute(task);
			
			break;
			
		default:
			System.out.println("Unrecognized Message");
			break;
		}
	}

	//================================================================================
	// Add links to crawl
	//================================================================================
	public void publishLink(FetchRequest request) {
		
		// Return if we're already at our max depth
		if (request.depth == Constants.depth) {
			return;
		}
		
		synchronized (state) {
			Page page = new Page(request.url, request.depth, request.domain);
			state.addPage(page);
			state.makrUrlPending(page);
			fetchURL(request);
		}
	}
	
	
	public void fetchURL(FetchRequest request) {
		
	}
	
	//================================================================================
	//================================================================================
	// Main
	//================================================================================
	//================================================================================
	public static void main(String[] args){

		int port = 0;
		int threads = 5;
		
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		}

		else if (args.length == 2){
			port = Integer.parseInt(args[0]);
			threads = Integer.parseInt(args[1]);
		}

		else {
			System.out.println("Usage: java node.Worker PORT <THREADS>");
			System.exit(1);
		}


		// Create node
		Worker worker = new Worker(port,threads);
		worker.initServer();

	}
}
