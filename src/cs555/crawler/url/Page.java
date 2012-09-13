package cs555.crawler.url;

import java.util.ArrayList;

import cs555.crawler.utilities.*;
import cs555.crawler.wireformats.FetchRequest;

public class Page {

	public int status;
	public String urlString;
	public int depth;
	public String domain;
	ArrayList<Page> links;
	
	//================================================================================
	// Constructor
	//================================================================================
	public Page(String url){
		urlString = url;
		domain = urlString;
		status = Constants.URL_Ready;
		depth = 0;
		links = new ArrayList<Page>();
	}
	
	public Page(String url, int dep, String d){
		urlString = url;
		domain = d;
		status = Constants.URL_Ready;
		depth = dep;
		links = new ArrayList<Page>();
	}
	
	//================================================================================
	// Modifiers
	//================================================================================
	public FetchRequest getFetchRequest(){
		return new FetchRequest(domain, depth, urlString, stringLinks());
	}
	
	public ArrayList<String> stringLinks(){
		ArrayList<String> stringLinks = new ArrayList<String>();
		
		for (Page p : links){
			stringLinks.add(p.urlString);
		}
		
		return stringLinks;
	}
	//================================================================================
	// House Keeping
	//================================================================================
	// Override .equals
	public boolean equals(Page other){
		if (this.urlString.equalsIgnoreCase(other.urlString)){
			return true;
		}
		return false;
	}
	
	// Override toString
	public String toString(){
		String s = "";
		
		s += urlString + " " + status;
		
		return s;
	}
}
