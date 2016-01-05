package lab13.problem;

import java.util.Observable;
import java.util.Observer;

public class NewNamePrinterObserver implements Observer {

	@Override
	public void update(Observable observ, Object param) {
		if (!(param instanceof FileEvent))
			throw new IllegalStateException();
		
		FileEvent fevent = (FileEvent)param;
		if (fevent.getType().equals("ENTRY_CREATE"))
			System.out.println("New file: " + fevent.getFname());
	}

}
