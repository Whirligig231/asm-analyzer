package analyzer.common;

import java.util.Observable;

public class ThreadObservableWrapper extends Observable implements Runnable {
	private Thread t;
	
	public ThreadObservableWrapper(Thread t){
		this.t = t;
	}
	
	public void join(){
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setChanged();
		this.notifyObservers(t);
	}

	@Override
	public void run() {
		t.start();
	}
	
	
}
