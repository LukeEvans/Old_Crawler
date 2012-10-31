package cs555.crawler.url;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageMetadata {


	ArrayList<String> links;
	HashMap<String, Integer> fileMap;

	//================================================================================
	// Constructor
	//================================================================================
	public PageMetadata() {
		links = new ArrayList<String>();
		fileMap = new HashMap<String, Integer>();
	}

	//================================================================================
	// Manipulation
	//================================================================================
	public void addFile(String s, int i) {
		int curr = 0;
		
		if (fileMap.containsKey(s)) {
			curr = fileMap.get(s);
		}
		
		curr += i;
		fileMap.put(s, curr);
	}
	
	public void parseFiles(HashMap<String, Integer> fileMap) {

	}
	
	public void addLinks(ArrayList<String> l) {
		
		for (String s : l) {
			if (!links.contains(s)) {
				links.add(s);
			}
		}
	}
	
	//================================================================================
	// House Keeping
	//================================================================================
	public String toString() {
		String s = "";
		
		s += "Unique links: " + links.size() + "\n";
		
		for (Map.Entry<String,Integer> entry : fileMap.entrySet()) {
			  String format = entry.getKey();
			  int count = entry.getValue();
			  
			  s += format + " = " + count + "\n";
			}
		
		return s;
	}
 }
