package cs555.crawler.pool;

import java.net.URL;

import org.htmlparser.beans.*;

import cs555.crawler.communications.Link;
import cs555.crawler.wireformats.*;

public class FetchParseTask implements Task {

	Link link;
	int runningThread;
	FetchRequest request;
	
	// Fetchers
	HTMLTextBean textFetcher;
	HTMLLinkBean linkfetcher;
	
	// URL
	String urlString;
	
	// Text
	String urlText;
	
	
	
	//================================================================================
	// Constructor
	//================================================================================
	public FetchParseTask(Link l, String url, FetchRequest req){
		link = l;
		textFetcher = new HTMLTextBean();
		linkfetcher = new HTMLLinkBean();
		urlString = url;
		request = req;
		
		textFetcher.setURL(url);
		linkfetcher.setURL(url);
	}
	
	//================================================================================
	// Run
	//================================================================================
	public void run() {
		URL[] links = linkfetcher.getLinks();
		
		FetchResponse response = new FetchResponse(request.domain, request.depth + 1, urlString, links);
		System.out.println("Sent: "+ response);
		link.sendData(response.marshall());		
	}

	
	
	//================================================================================
	// House Keeping
	//================================================================================
	public void setRunning(int i) {
		runningThread = i;
	}

}
