package cs555.crawler.node;

import cs555.crawler.communications.Link;
import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;
import cs555.crawler.wireformats.ElectionMessage;
import cs555.crawler.wireformats.FetchRequest;
import cs555.crawler.wireformats.Verification;
import cs555.crawler.pool.*;

public class Worker extends Node{

	Link nodeManagerLink;
	ThreadPoolManager poolManager;

	//================================================================================
	// Constructor
	//================================================================================
	public Worker(int port,int threads){
		super(port);
		nodeManagerLink = null;
		poolManager = new ThreadPoolManager(threads);
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
			
			Verification electionReply = new Verification(election.number);
			l.sendData(electionReply.marshall());
			nodeManagerLink = l;
			
			System.out.println("Elected Official");
			
			break;

		case Constants.Fetch_Request:
			FetchRequest request = new FetchRequest();
			request.unmarshall(bytes);
			
			System.out.println("Got: \n" + request);
			
			FetchParseTask task = new FetchParseTask(nodeManagerLink, request.url, request);
			//poolManager.execute(task);
			task.start();
			
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
