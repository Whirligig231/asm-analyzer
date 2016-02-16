package analyzer.gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import analyzer.pipeline.LoadingInfo;

public class LoadingObserver implements Observer {
	
	private JLabel label;
	private JProgressBar progress;
	
	public LoadingObserver(JLabel label, JProgressBar progress) {
		super();
		this.label = label;
		this.progress = progress;
	}

	@Override
	public void update(Observable o, Object arg) {
//		System.out.println("Update in LoadingObserver");
		LoadingInfo li = (LoadingInfo)arg;
		this.label.setText(li.getMessage());
		this.label.repaint();
		this.progress.setValue((int) li.getProgress());
		this.progress.repaint();
	}
	
	

}
