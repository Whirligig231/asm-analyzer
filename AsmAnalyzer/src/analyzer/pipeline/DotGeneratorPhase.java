package analyzer.pipeline;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.IModel;
import analyzer.visitor.output.ClassUmlOutputStream;

public class DotGeneratorPhase extends Observable implements IPhase, Observer {
	
	private IModel model;
	private String outputDir, pathToDot;
	private String[] classes;

	public DotGeneratorPhase(IModel model, String outputDir, String pathToDot) {
		this.model = model;
		this.outputDir = outputDir;
		this.pathToDot = pathToDot;
	}
	
	public String[] getClasses() {
		return classes;
	}

	public void setClasses(String[] classes) {
		this.classes = classes;
	}

	@Override
	public void run() throws IOException, InterruptedException, IllegalStateException {
		
		OutputStream os = new FileOutputStream(outputDir + "/output.dot");
		ClassUmlOutputStream classUmlOutputStream = new ClassUmlOutputStream(os);
		classUmlOutputStream.addObserver(this);
		
		for (String className : classes) {
			classUmlOutputStream.addClassName(ClassNameStandardizer.standardize(className));
		}
		classUmlOutputStream.write(model);
		classUmlOutputStream.close();
		
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(this.pathToDot + " " + outputDir + "/output.dot -o " + outputDir
				+ "/output.png -Tpng");
		
		int returnValue = pr.waitFor();
		if (returnValue != 0) {
			throw new IllegalStateException("DOT has unexpectedly failed");
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		this.notifyObservers(arg);
	}

}
