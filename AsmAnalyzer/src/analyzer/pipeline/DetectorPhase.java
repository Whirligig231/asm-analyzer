package analyzer.pipeline;

import java.util.Observable;
import java.util.Observer;

import analyzer.model.IModel;
import analyzer.visitor.detect.IPatternDetector;
import analyzer.visitor.detect.ObservablePatternDetector;

public class DetectorPhase extends Observable implements IPhase, Observer {
	
	private IPatternDetector detector;
	private IModel model;
	
	public DetectorPhase(ObservablePatternDetector detector, IModel model) {
		detector.addObserver(this);
		this.detector = detector;
		this.model = model;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers(arg);
	}

	@Override
	public void run() {
		this.detector.detect(this.model);
	}

}
