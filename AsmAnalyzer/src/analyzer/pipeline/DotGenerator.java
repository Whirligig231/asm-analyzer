package analyzer.pipeline;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.IModel;
import analyzer.visitor.output.ClassUmlOutputStream;

public class DotGenerator {
	
	private IModel model;
	private String outputDir, pathToDot;
	
	public DotGenerator(IModel model, String outputDir, String pathToDot) {
		this.model = model;
		this.outputDir = outputDir;
		this.pathToDot = pathToDot;
	}
	
	public void generateDiagram(String[] classes) throws IOException, InterruptedException {
		
		OutputStream os = new FileOutputStream(outputDir + "/output.dot");
		ClassUmlOutputStream classUmlOutputStream = new ClassUmlOutputStream(os);
		
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

}
