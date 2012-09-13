package cs555.crawler.pool;

import java.net.URL;

//import org.htmlparser.beans.*;

import cs555.crawler.communications.Link;
import cs555.crawler.wireformats.*;

public class FetchParseTask implements Task{

	Link link;
	int runningThread;
	FetchRequest request;
	
	// Fetchers
	//HTMLTextBean textFetcher;
	//HTMLLinkBean linkfetcher;
	
	// URL
	String urlString;
	
	// Text
	String urlText;
	
	
	
	//================================================================================
	// Constructor
	//================================================================================
	public FetchParseTask(String url, FetchRequest req){
		//textFetcher = new HTMLTextBean();
		//linkfetcher = new HTMLLinkBean();
		
		request = req;
		
		//textFetcher.setURL(url);
		//linkfetcher.setURL(url);
	}
	
	//================================================================================
	// Run
	//================================================================================
	public void run() {
//		URL[] links = linkfetcher.getLinks();
//		FetchResponse response = new FetchResponse(request.domain, request.depth + 1, urlString, links);
//		link.sendData(response.marshall());
//		
//		String text = textFetcher.getText();
//		SaveTask saver = new SaveTask(urlString, text);
//		saver.run();
		
		System.out.println("Fetching: " + urlString);
	}

	
	
	//================================================================================
	// House Keeping
	//================================================================================
	public void setRunning(int i) {
		runningThread = i;
	}

}
