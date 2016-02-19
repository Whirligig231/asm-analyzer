package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observer;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import analyzer.common.ThreadObservableWrapper;
import analyzer.model.IModel;
import analyzer.model.Model;
import analyzer.pipeline.IPhase;
import analyzer.pipeline.IPhaseFactory;
import analyzer.pipeline.IPipeline;
import analyzer.pipeline.PhaseFactory;
import analyzer.pipeline.Pipeline;

public class LandingFrame extends JFrame {

	private static final long serialVersionUID = 322559617349772310L;
	
	private JButton analyze;
	private Properties config;
	private JFileChooser fc;
	
	private Observer lo;
	private IPipeline pipeline;
	public IModel model;

	private IErrorHandler errorHandler;
	
	public LandingFrame() {
		
		this.errorHandler = new DialogErrorHandler(this);
		
		this.setTitle("Design Parser");
		this.setSize(300, 170);
		this.setLocationRelativeTo(null); // Center frame on the screen
		this.setLayout(new BorderLayout());
		
		JComponent main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

		JComponent top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
		top.add(Box.createHorizontalGlue());
		
		JButton loadConfig = new JButton("Load Config");
		top.add(loadConfig);
		top.add(Box.createHorizontalStrut(10));
		
		this.analyze = new JButton("Analyze");
		top.add(this.analyze);
		top.add(Box.createHorizontalGlue());
		
		this.analyze.setEnabled(false);
		
		main.add(top);
		main.add(Box.createVerticalStrut(20));
		
		JComponent bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
		
		JLabel loadingText = new JLabel("Loading ...");
		bottom.add(loadingText);
		bottom.add(Box.createVerticalStrut(5));
		
		JProgressBar loadingBar = new JProgressBar();
		loadingBar.setValue(0);
		bottom.add(loadingBar);
		
		bottom.setVisible(false);
		
		main.add(bottom);
		
		this.add(main, BorderLayout.CENTER);
		this.add(Box.createVerticalStrut(20), BorderLayout.PAGE_START);
		this.add(Box.createHorizontalStrut(20), BorderLayout.LINE_START);
		this.add(Box.createHorizontalStrut(20), BorderLayout.LINE_END);
		this.add(Box.createVerticalStrut(20), BorderLayout.PAGE_END);
		
		loadConfig.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LandingFrame.this.getConfigFile();
				LandingFrame.this.setupPipeline();
			}
			
		});
		
		this.analyze.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bottom.setVisible(true);
				LandingFrame.this.executePipeline();
			}
			
		});
		
		this.lo = new LoadingObserver(loadingText, loadingBar);
		
		this.fc = new JFileChooser();
		FileFilter filter = 
				new FileNameExtensionFilter("Java Properties Files (.properties)", "properties");
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		
	}

	private void getConfigFile() {
		int returnVal = this.fc.showOpenDialog(this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {	
			File configFile = fc.getSelectedFile();
			
			try {
				this.analyze.setEnabled(false);
				this.loadConfig(configFile);
				this.analyze.setEnabled(true);
			} catch (FileNotFoundException e) {
				this.error("could not open the config file");
			} catch (IOException | IllegalArgumentException e) {
				this.error("could not parse the config file");
			}
		}
	}
	
	private void loadConfig(File configFile) throws IOException {
		this.config = new Properties();
		InputStream in = new FileInputStream(configFile);
		this.config.load(in);
		in.close();
	}
	
	private void error(String errorMsg) {
		this.errorHandler.handleError(errorMsg);
	}

	private void setupPipeline() {
		
		this.model = new Model();
		this.pipeline = new Pipeline();		
		this.pipeline.addObserver(this.lo);
		IPhaseFactory factory = new PhaseFactory();
		
		String phaseLine = this.config.getProperty("Phases");
		if (phaseLine == null) {
			this.error("Phases must be set");
			return;
		}
		
		String[] phases = phaseLine.split("[;, ]");
		for (String phaseName : phases) {
			if (phaseName.equals(""))
				continue;
			
			IPhase phase;
			try {
				phase = factory.makePhase(phaseName, this.model, this.config);
				this.pipeline.addPhase(phase);
			} catch (Exception e) {
				this.error(e.getMessage());
				return;
			}
		}

		
	}
	
	private void executePipeline() {
		Runnable pipelineRunner = new PipelineRunner(this.pipeline, this.errorHandler);
		Thread t = new Thread(pipelineRunner);
		ThreadObservableWrapper tow = new ThreadObservableWrapper(t);
		t.start();
		/*
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		JFrame results = new ResultFrame(this.model, this.config);
		results.setVisible(true);
		results.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		
	}

}
