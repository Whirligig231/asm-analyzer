package lab13.problem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class ModificationBackwardsPrintObserver implements Observer {

	@Override
	public void update(Observable observ, Object param) {
		if (!(param instanceof FileEvent))
			throw new IllegalStateException();
		
		FileEvent fevent = (FileEvent)param;
		if (fevent.getType().equals("ENTRY_MODIFY")) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(fevent.getFname()));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append('\n');
				}
				
				br.close();
				
				sb.reverse();
				System.out.println(sb.substring(1));
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
