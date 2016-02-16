package analyzer.visitor.detect;

import java.util.Observable;

import analyzer.model.IModel;

public abstract class ObservablePatternDetector extends Observable implements IPatternDetector {

	@Override
	public abstract void detect(IModel model);
	
	protected void classVisitUpdate(String className) {
		this.setChanged();
		this.notifyObservers("Analyzing class " + className + " ...");
	}

}
