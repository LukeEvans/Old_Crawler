package cs555.crawler.utilities;

// Constants for message types
public class Constants {

	// Message types
	public static final int Payload = 1;
	public static final int Verification = 2;
	public static final int Node_Results = 3;
	public static final int Node_Complete = 4;
	public static final int Election_Message = 5;
	
	// URL states
	public static final int URL_Ready = 5;
	public static final int URL_Pending = 6;
	public static final int URL_Complete = 7;
	
	
	public static final int Failure = 99;
	public static final int Success = 100;
	
	// Message sizes
	public static final int LEN_BYTES = 2048;
}
