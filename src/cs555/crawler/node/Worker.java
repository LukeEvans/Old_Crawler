package cs555.crawler.node;

import cs555.crawler.communications.Link;
import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;
import cs555.crawler.wireformats.ElectionMessage;


public class Worker extends Node{

	Link nodeManagerLink;

	//================================================================================
	// Constructor
	//================================================================================
	public Worker(int port){
		super(port);
		nodeManagerLink = null;

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
			
			nodeManagerLink = l;
			
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

		if (args.length == 3) {
			port = Integer.parseInt(args[0]);
		}

		else if (args.length == 4){
			port = Integer.parseInt(args[0]);
		}

		else {
			System.out.println("Usage: java node.Worker PORT");
			System.exit(1);
		}


		// Create node
		Worker worker = new Worker(port);
		worker.initServer();

	}
}
