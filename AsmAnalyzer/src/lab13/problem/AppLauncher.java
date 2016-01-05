package lab13.problem;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observer;

public class AppLauncher extends Thread {
	
	private final WatchService watcher;
	private final Path dir;
	private final DirectoryObservable observ;
	private boolean stop;
	private Collection<Process> processes;
	
	/**
	 * Creates a WatchService and registers the given directory
	 */
	AppLauncher(Path dir) throws IOException {
		this.stop = true;
		this.dir = dir;
		this.processes = Collections.synchronizedList(new ArrayList<Process>());
		this.watcher = FileSystems.getDefault().newWatchService();
		dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		
		this.observ = new DirectoryObservable(this);
	}
	
	/**
	 * Process all events for keys queued to the watcher
	 */
	public void run() {
		this.stop = false;
		while(!stop) {
			// Wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			// Context for directory entry event is the file name of entry
			List<WatchEvent<?>> events = key.pollEvents();
			if(!events.isEmpty()) {
				@SuppressWarnings("unchecked")
				WatchEvent<Path> event = (WatchEvent<Path>)events.get(0);
				Path name = event.context();
				Path child = dir.resolve(name);

				// Call the handler method
				this.handleDirectoryEvent(event.kind().name(), child);
			}

			// Reset key and remove from set if directory no longer accessible
			if (!key.reset()) {
				break;
			}
		}

		// We gracefully stopped the service now, let's delete the temp file
		this.clearEverything();
	}

	/**
	 * This method is for internal use to delete the temporary file created by
	 * the {@link #clearEverything()} method. As well as to kill all of the newly
	 * created process.
	 */
	protected void clearEverything() {
		File file = new File(dir.toFile() + "/.temp");
		file.delete();
		
		for(Process p: this.processes) {
			p.destroy();
		}
	}

	/**
	 * This method gracefully stops the WatchDir service.
	 * @throws IOException
	 */
	public void stopGracefully() throws IOException {
		this.stop = true;
		File file = new File(dir.toFile() + "/.temp");

		// Let's force the while loop in the run method to compe out of the blocking watcher.take() call here
		// You can also create a directory by calling file.mkdir()
		file.createNewFile();
	}
	
	/**
	 * Returns true if the launcher is running, otherwise false.
	 */
	public boolean isRunning() {
		return !stop;
	}
	
	/**
	 * Returns the number of applications launched so far by the launcher.
	 */
	public int getApplicationsCount() {
		return this.processes.size();
	}

	/**
	 * This method gets called when ever the directory being monitored changes.

	 * @param eventName One of the following three strings:
	 * <ol>
	 * 	<li>ENTRY_CREATE - When a file/folder gets created.</li>
	 * 	<li>ENTRY_DELETE - When a file/folder gets deleted.</li>
	 * 	<li>ENTRY_MODIFY - When a file/folder get modified.</li>
	 * </ol>     
	 * @param file The file that generate the event
	 * @throws IOException 
	 */
	public void handleDirectoryEvent(String eventName, Path file) {
		
		observ.notifyObservers(new FileEvent(eventName, file.toString()));
		
	}

	/**
	 * Adds an observer to observe the directory at runtime.
	 * 
	 * @param observer The observer to use
	 */
	public void addFileObserver(Observer observer) {
		this.observ.addObserver(observer);
	}

	public void startCommand(String command, String fname) {

		ProcessBuilder processBuilder = null;

		// Run the application if support is available
		try {
			System.out.format("Launching %s ...%n", command);
			processBuilder = new ProcessBuilder(command, fname);
			
			// Start and add the process to the processes list
			Process process = processBuilder.start();
			this.processes.add(process);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
