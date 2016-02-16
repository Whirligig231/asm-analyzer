package analyzer.pipeline;

import java.io.IOException;
import java.util.Observer;

public interface IPipeline {
	
	public void addObserver(Observer observer);
	
	public void addPhase(IPhase phase);
	
	public void run() throws IOException, InterruptedException, IllegalStateException;

}
