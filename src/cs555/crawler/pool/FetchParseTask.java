package cs555.crawler.pool;

import java.net.URL;
import java.util.ArrayList;

import org.htmlparser.beans.*;

import cs555.crawler.communications.Link;
import cs555.crawler.wireformats.FetchResponse;

public class FetchParseTask implements Task{

	Link link;
	int runningThread;
	
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
	public FetchParseTask(String url){
		textFetcher = new HTMLTextBean();
		linkfetcher = new HTMLLinkBean();
		
		textFetcher.setURL(url);
		linkfetcher.setURL(url);
	}
	
	//================================================================================
	// Run
	//================================================================================
	public void run() {
		URL[] links = linkfetcher.getLinks();
		
		
		String text = textFetcher.getText();
		SaveTask saver = new SaveTask(urlString, text);
		saver.run();
	}

	
	
	//================================================================================
	// House Keeping
	//================================================================================
	public void setRunning(int i) {
		runningThread = i;
	}

}
