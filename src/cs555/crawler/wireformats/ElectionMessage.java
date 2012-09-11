package cs555.crawler.wireformats;

import cs555.crawler.utilities.Constants;

public class ElectionMessage extends Payload {

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public ElectionMessage(int number){
		super(number);
		type = Constants.Election_Message;
	}
	
	public ElectionMessage(){
		super();
		type = Constants.Election_Message;
	}
}
