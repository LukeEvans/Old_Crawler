package cs555.crawler.pool;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.htmlparser.beans.*;

import cs555.crawler.node.Worker;
import cs555.crawler.url.Page;
import cs555.crawler.utilities.Constants;
import cs555.crawler.wireformats.FetchRequest;

public class FetchParseTask implements Task {

	int runningThread;

	// Fetchers
	HTMLTextBean textfetcher;
	LinkBean linkfetcher;

	// URL
	String urlString;

	// Text
	String urlText;

	// Request 
	FetchRequest request;
	
	Worker node;
	Page page;


	//================================================================================
	// Constructor
	//================================================================================
	public FetchParseTask(Page p, FetchRequest urlReq, Worker w){
		textfetcher = new HTMLTextBean();
		linkfetcher = new LinkBean();
		urlString = urlReq.url;
		request = urlReq;

		textfetcher.setURL(urlString);
		linkfetcher.setURL(urlString);
		
		node = w;
		page = p;
	}

	//================================================================================
	// Run
	//================================================================================
	public void run() {

		try {
			// URL
			URL url = new URL(urlString);
			
			// URL connections
			URLConnection linkURLConnection;
			linkURLConnection = url.openConnection();

			URLConnection textURLConnection;
			textURLConnection = url.openConnection();
			
			linkfetcher.setConnection(linkURLConnection);
			textfetcher.setConnection(textURLConnection);

			URL [] urls = linkfetcher.getLinks();
			//String webString = textfetcher.getText();	
			
			ArrayList<String> freshLinks = removeBadDomains(stringURLs(urls));
			
//			for (String s : freshLinks) {
//				System.out.println(s);
//			}
			
			node.linkComplete(page, freshLinks, getFileMap(urls));
			
			// Save webString to file in basePath + /url
			//System.out.println(webString);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		return;
	}


	//================================================================================
	// Parse
	//================================================================================
	public HashMap<String, Integer> getFileMap(URL[] links) {
		int html = 0;
		int htm = 0;
		int doc = 0;
		int pdf = 0;
		int cfm = 0;
		int aspx = 0;
		int asp = 0;
		int php = 0;
		
		for (URL u : links) {
			String s = u.toString();
			
			if (s.endsWith(".html")) html++;
			else if (s.endsWith(".htm")) htm++;
			else if (s.endsWith(".doc")) doc++;
			else if (s.endsWith(".pdf")) pdf++;
			else if (s.endsWith(".cfm")) cfm++;
			else if (s.endsWith(".aspx")) aspx++;
			else if (s.endsWith(".asp")) asp++;
			else if (s.endsWith(".php")) php++;
		}
		
		
		HashMap<String, Integer> fileMap = new HashMap<String, Integer>();
		
		fileMap.put("html", html);
		fileMap.put("htm", htm);
		fileMap.put("doc", doc);
		fileMap.put("pdf", pdf);
		fileMap.put("cfm", cfm);
		fileMap.put("aspx", aspx);
		fileMap.put("asp", asp);
		fileMap.put("phpl", php);
		
		
		return fileMap;
	}

	public ArrayList<String> stringURLs(URL[] urls) {
		ArrayList<String> strings = new ArrayList<String>();
		
		for (URL u : urls) {
			strings.add(u.toString());
		}
		
		return strings;
	}

	public ArrayList<String> removeBadDomains(ArrayList<String> original) {
		ArrayList<String> newList = new ArrayList<String>();
		
		for (String s : original) {
			for (String d : Constants.domains) {
				if (s.contains("." + d)) {
					newList.add(s);
					continue;
				}
			}
		}
		
		return newList;
	}
	
	@Override
	public void setRunning(int i) {
		// TODO Auto-generated method stub
		
	}


}
