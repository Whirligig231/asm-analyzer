package analyzer.asmvisitor.visitclass;

import java.util.Observable;

public class ClassInputObservable extends Observable {
	
	public void classVisitUpdate(String className) {
//		System.out.println("Notify observers from ClassInputObservable");
		this.setChanged();
		this.notifyObservers("Reading class " + className + " ...");
	}

}
