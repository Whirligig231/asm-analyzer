package analyzer.asmvisitor.visitclass;

import java.util.Observable;

public class ClassInputObservable extends Observable {
	
	public void classVisitUpdate(String className) {
		this.setChanged();
		this.notifyObservers("Reading class " + className + " ...");
	}

}
