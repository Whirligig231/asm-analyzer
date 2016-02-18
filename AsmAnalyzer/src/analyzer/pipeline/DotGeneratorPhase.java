package analyzer.pipeline;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.IModel;
import analyzer.visitor.output.ClassUmlOutputStream;

public class DotGeneratorPhase extends Observable implements IPhase, Observer {
	
	private IModel model;
	private Properties properties;

	public DotGeneratorPhase(IModel model, Properties properties) {
		this.model = model;
		this.properties = properties;
	}

	@Override
	public void run() throws IOException, InterruptedException, IllegalStateException {
		
		String outputDir = properties.getProperty("Output-Directory");
		if (outputDir == null) {
			throw new IOException("Output-Directory must be set");
		}
		String dotPath = properties.getProperty("Dot-Path");
		if (dotPath == null) {
			throw new IOException("Dot-Path must be set");
		}
		String dotClasses = properties.getProperty("Input-Classes");
		if (dotClasses == null) {
			throw new IOException("Input-Classes must be set");
		}
		String[] dotClassNames = dotClasses.split("[;, ]");
		
		OutputStream os = new FileOutputStream(outputDir + "/output.dot");
		ClassUmlOutputStream classUmlOutputStream = new ClassUmlOutputStream(os);
		classUmlOutputStream.addObserver(this);
		
		for (String className : dotClassNames) {
			classUmlOutputStream.addClassName(ClassNameStandardizer.standardize(className));
		}
		classUmlOutputStream.write(model);
		classUmlOutputStream.close();
		
		this.setChanged();
		this.notifyObservers("Running DOT ...");
		
		Runtime rt = Runtime.getRuntime();
		String cmd = dotPath + " \"" + outputDir + "/output.dot\" -o \"" + outputDir
				+ "/output.png\" -Tpng";
		Process pr = rt.exec(cmd);
		
		int returnValue = pr.waitFor();
		if (returnValue != 0) {
			throw new IllegalStateException("DOT has unexpectedly failed (\"" + cmd +
					"\" returned code " + returnValue + ")");
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers(arg);
	}

}
