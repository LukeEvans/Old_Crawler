package cs555.crawler.url;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

import cs555.crawler.utilities.*;

public class CrawlerState {

	Stack<URL> readyStack;
	ArrayList<URL> pendingList;
	ArrayList<URL> doneList;
	
	String linkFile;
	
	//================================================================================
	// Constructor
	//================================================================================
	public CrawlerState(String lf){
		linkFile = lf;
		
		readyStack = new Stack<URL>();
		pendingList = new ArrayList<URL>();
		doneList = new ArrayList<URL>();
		
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
			URL url = new URL(urlString);
			addURL(url);
			
		}
	}
	
	
	//================================================================================
	// Accessor
	//================================================================================
	// Get next ready URL
	public URL getNextReadyURL(){
		
		if (!readyStack.isEmpty()){
			URL url = readyStack.pop();
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
	public void addURL(URL u){
		if (!contains(u)){
			readyStack.add(u);
		}
	}
	
	public URL findPendingUrl(URL u){
		for (URL url : pendingList){
			if (url.equals(u)){
				return url;
			}
		}
		
		return null;
	}
	
	// Mark complete
	public void markUrlComplete(URL u){
		URL url = findPendingUrl(u);
		
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
	public boolean contains(URL url) {

		for (URL u : readyStack){
			if (url.equals(u)){
				return true;
			}
		}
		
		for (URL u : pendingList){
			if (url.equals(u)){
				return true;
			}
		}

		for (URL u : doneList){
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
		for (URL u : readyStack){
			s += u.toString() + "\n";
		}
		
		s += "\nPending:\n";
		
		for (URL u : pendingList){
			s += u.toString() + "\n";
		}
		
		s += "\nDone:\n";
		
		for (URL u : doneList){
			s += u.toString() + "\n";
		}
		
		return s;
	}
}
