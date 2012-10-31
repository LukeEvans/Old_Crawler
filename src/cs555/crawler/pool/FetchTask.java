package cs555.crawler.pool;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cs555.crawler.node.Worker;
import cs555.crawler.url.Page;
import cs555.crawler.utilities.Constants;
import cs555.crawler.wireformats.FetchRequest;

public class FetchTask implements Task {

	int runningThread;


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
	public FetchTask(Page p, FetchRequest urlReq, Worker w){
		urlString = urlReq.url;
		request = urlReq;

		node = w;
		page = p;
	}

	//================================================================================
	// Run
	//================================================================================
	public void run() {

		try {

			ArrayList<String> urls = new ArrayList<String>();

			Document doc = Jsoup.connect(urlString).timeout(6000).get();

			Elements links = doc.select("a[href]");
			String text = doc.body().text();

			for (Element link : links) {
				urls.add(link.attr("abs:href"));
			}

			ArrayList<String> freshLinks = removeBadDomains(urls);
			node.linkComplete(page, freshLinks, getFileMap(urls));

			SaveTask saver = new SaveTask(urlString, text);
			saver.save();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Error : " + e);
			node.linkErrored(page);
			return;
		}

		return;
	}


	//================================================================================
	// Parse
	//================================================================================
	public HashMap<String, Integer> getFileMap(ArrayList<String> links) {
		int html = 0;
		int htm = 0;
		int doc = 0;
		int pdf = 0;
		int cfm = 0;
		int aspx = 0;
		int asp = 0;
		int php = 0;

		for (String s : links) {

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
		fileMap.put("php", php);


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

					if (!linkIsFile(s)) { 
						newList.add(s);
						continue;
					}
				}
			}
		}

		return newList;
	}

	public boolean linkIsFile(String link) {
		List<String> ext = Arrays.asList("doc", "pdf", "jpg", "png", "gif");

		for (String e : ext) {
			if (link.toLowerCase().endsWith("." + e)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void setRunning(int i) {
		// TODO Auto-generated method stub

	}


}
