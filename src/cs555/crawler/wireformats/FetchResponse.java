package cs555.crawler.wireformats;

import java.net.URL;
import java.util.ArrayList;

import cs555.crawler.utilities.Constants;

public class FetchResponse extends FetchRequest{

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public FetchResponse(String d, int dep, String u, ArrayList<String> list){
		init(d, dep, u, list);
		type = Constants.Fetch_Response;
		
	}
	
	public FetchResponse(String d, int dep, String u, URL[] urls){
		init(d, dep, u, removeUnrelatedLinks(urls));
		type = Constants.Fetch_Response;
	}
	
	public FetchResponse(){
		init("", 0, "", new ArrayList<String>());
		type = Constants.Fetch_Response;
	}
}
