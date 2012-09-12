package cs555.crawler.url;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

import cs555.crawler.utilities.*;

public class CrawlerState {

	Stack<Page> readyStack;
	ArrayList<Page> pendingList;
	ArrayList<Page> doneList;
	
	String linkFile;
	
	//================================================================================
	// Constructor
	//================================================================================
	public CrawlerState(String lf){
		linkFile = lf;
		
		readyStack = new Stack<Page>();
		pendingList = new ArrayList<Page>();
		doneList = new ArrayList<Page>();
		
		buildState();
	}
	
	public void buildState(){
		try{
			// Open the file that is the first 
			FileInputStream fstream = new FileInputStream(linkFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				createURLFromLine(strLine);
			}
			
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	// Turn a line of text into a peer
	public void createURLFromLine(String line){
		String[] stringParts = line.split("\\s+");
		
		if (stringParts.length == 2){
			String urlString = stringParts[0];
			Page url = new Page(urlString);
			addPage(url);
			
		}
	}
	
	
	//================================================================================
	// Accessor
	//================================================================================
	// Get next ready URL
	public Page getNextReadyPage(){
		
		if (!readyStack.isEmpty()){
			Page url = readyStack.pop();
			url.status = Constants.URL_Pending;
			pendingList.add(url);
			
			return url;
		}
		
		return null;
	}
	
	//================================================================================
	// List manipulation 
	//================================================================================
	// Add peer
	public void addPage(Page u){
		if (!contains(u)){
			readyStack.add(u);
		}
	}
	
	public Page findPendingUrl(Page u){
		for (Page url : pendingList){
			if (url.equals(u)){
				return url;
			}
		}
		
		return null;
	}
	
	// Mark complete
	public void markUrlComplete(Page u){
		Page url = findPendingUrl(u);
		
		if (url != null){
			url.status = Constants.URL_Complete;
			pendingList.remove(url);
			doneList.add(url);
		}
	}
	
	//================================================================================
	// House Keeping
	//================================================================================
	// Override .contains method
	public boolean contains(Page url) {

		for (Page u : readyStack){
			if (url.equals(u)){
				return true;
			}
		}
		
		for (Page u : pendingList){
			if (url.equals(u)){
				return true;
			}
		}

		for (Page u : doneList){
			if (url.equals(u)){
				return true;
			}
		}
		
		return false;
	}

	// Override .toString method
	public String toString() {
		String s = "";

		s += "\nReady:\n";
		for (Page u : readyStack){
			s += u.toString() + "\n";
		}
		
		s += "\nPending:\n";
		
		for (Page u : pendingList){
			s += u.toString() + "\n";
		}
		
		s += "\nDone:\n";
		
		for (Page u : doneList){
			s += u.toString() + "\n";
		}
		
		return s;
	}
}
