package cs555.crawler.pool;

import java.io.IOException;

/**
 * 
 * @author levans
 * Task is an interface to be implemented by specific tasks. Eg send, receive...
 */
public interface Task extends Runnable{
	
	@Override
	public void run();

	public void setRunning(int i);
}
