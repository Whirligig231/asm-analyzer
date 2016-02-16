package analyzer.visitor.output;

import java.util.Observable;

public class ClassOutputObservable extends Observable {
	
	public void classVisitUpdate(String className) {
		this.setChanged();
		this.notifyObservers("Printing class " + className + " ...");
	}

}
