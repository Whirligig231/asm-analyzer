package analyzer.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DialogErrorHandler implements IErrorHandler {
	
	private JFrame frame;
	
	public DialogErrorHandler(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void handleError(String errorMsg) {
		JOptionPane.showMessageDialog(this.frame, "Error: " + errorMsg, "An error occurred", 
				JOptionPane.ERROR_MESSAGE);
	}

}
