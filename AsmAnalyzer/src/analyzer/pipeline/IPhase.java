package analyzer.pipeline;

import java.io.IOException;
import java.util.Observer;

public interface IPhase {

	public void addObserver(Observer observer);
	
	public void run() throws IOException, InterruptedException, IllegalStateException;
	
}