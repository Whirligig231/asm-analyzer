package lab13.problem;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class FileOpenObserver implements Observer {
	
	private Map<String, IFileOpenBehavior> behaviors;
	
	public FileOpenObserver() {
		this.behaviors = new HashMap<>();
	}
	
	public void associate(String extension, IFileOpenBehavior behavior) {
		this.behaviors.put(extension, behavior);
	}

	@Override
	public void update(Observable observ, Object param) {
		
		if (!(param instanceof FileEvent))
			throw new IllegalStateException();
		FileEvent fevent = (FileEvent)param;
		
		if (!(observ instanceof DirectoryObservable))
			throw new IllegalStateException();
		DirectoryObservable dobserv = (DirectoryObservable)observ;
		
		if (fevent.getType().equals("ENTRY_CREATE")) {
			String fileName = fevent.getFname();
			System.out.println("Processing " + fileName + "...");
		
			String extension;
			
			try {
				extension = fileName.substring(fileName.lastIndexOf('.'));
			} catch (IndexOutOfBoundsException ex) {
				extension = "";
			}
			
			if (!this.behaviors.containsKey(extension)) {
				System.err.format("No support available for: %s...%n", fileName);
				return;
			}
			
			dobserv.launch(this.behaviors.get(extension).getCommand(fileName), fileName);
		}
	}

}
