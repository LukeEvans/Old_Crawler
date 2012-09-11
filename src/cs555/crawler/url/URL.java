package cs555.crawler.url;

import cs555.crawler.utilities.*;

public class URL {

	public int status;
	public String urlString;
	
	//================================================================================
	// Constructor
	//================================================================================
	public URL(String url){
		urlString = url;
		status = Constants.URL_Ready;
	}
	

	
	//================================================================================
	// House Keeping
	//================================================================================
	// Override .equals
	public boolean equals(URL other){
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
