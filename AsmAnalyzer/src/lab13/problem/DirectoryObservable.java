package lab13.problem;

import java.util.Observable;

public class DirectoryObservable extends Observable {
	
	private AppLauncher launcher;
	
	public DirectoryObservable(AppLauncher launcher) {
		this.launcher = launcher;
	}
	
	@Override
	public void notifyObservers(Object obj) {
		this.setChanged();
		super.notifyObservers(obj);
		this.clearChanged();
	}
	
	public void launch(String command, String fname) {
		launcher.startCommand(command, fname);
	}

}
