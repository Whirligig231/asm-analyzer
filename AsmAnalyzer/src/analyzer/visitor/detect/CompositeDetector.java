package analyzer.visitor.detect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import analyzer.model.IModel;

public class CompositeDetector extends ObservablePatternDetector implements Observer {
	
	private Collection<IPatternDetector> detectors;
	
	public CompositeDetector() {
		this.detectors = new ArrayList<>();
	}
	
	public void addDetector(IPatternDetector detector) {
		this.detectors.add(detector);
		if (detector instanceof ObservablePatternDetector) {
			((ObservablePatternDetector)detector).addObserver(this);
		}
	}

	@Override
	public void detect(IModel model) {
		for (IPatternDetector detector : this.detectors)
			detector.detect(model);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers(arg);
	}

}
