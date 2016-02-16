package analyzer.pipeline;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

public class Pipeline extends Observable implements IPipeline, Observer {
	
	private Queue<IPhase> phases;
	private float currentProgress;
	private String currentMessage;
	
	public Pipeline() {
		this.phases = new LinkedList<>();
		this.currentProgress = 0;
		this.currentMessage = "Starting ...";
	}

	@Override
	public void addPhase(IPhase phase) {
		this.phases.add(phase);
		phase.addObserver(this);
	}

	@Override
	public void run() throws IOException, InterruptedException, IllegalStateException {
		int totalPhases = this.phases.size();
		int currentPhase = 0;
		for (IPhase phase : this.phases) {
			this.currentProgress = ((float)currentPhase) / totalPhases;
			this.changeLoading();
			phase.run();
			currentPhase++;
		}
		this.currentProgress = 1;
		this.currentMessage = "Done!";
		this.changeLoading();
	}

	@Override
	public void update(Observable o, Object arg) {
		this.currentMessage = arg.toString();
		this.changeLoading();
	}
	
	private void changeLoading() {
		this.setChanged();
		this.notifyObservers(new LoadingInfo(this.currentProgress, this.currentMessage));
	}

}
