package analyzer.pipeline;

import java.io.IOException;
import java.util.Properties;

import analyzer.model.IModel;
import analyzer.visitor.detect.AdapterPatternDetector;
import analyzer.visitor.detect.ClassPatternDetector;
import analyzer.visitor.detect.CompositePatternDetector;
import analyzer.visitor.detect.DecoratorPatternDetector;
import analyzer.visitor.detect.SingletonPatternDetector;

public class PhaseFactory implements IPhaseFactory {

	@Override
	public IPhase makePhase(String phaseType, IModel model, Properties properties) throws IOException {
		
		switch (phaseType) {
		case "Class-Loading":
			AsmReaderPhase phase = new AsmReaderPhase(model, properties);
			return phase;
		case "Decorator-Detection":
			int dThreshold = this.getIntProperty(properties, "Decorator-MethodDelegation", 3);
			return new DetectorPhase(new DecoratorPatternDetector(dThreshold), model);
		case "Adapter-Detection":
			int aThreshold = this.getIntProperty(properties, "Adapter-MethodDelegation", 3);
			return new DetectorPhase(new AdapterPatternDetector(aThreshold), model);
		case "Singleton-Detection":
			return new DetectorPhase(new SingletonPatternDetector(), model);
		case "Composite-Detection":
			return new DetectorPhase(new CompositePatternDetector(), model);
		case "Class-Detection":
			return new DetectorPhase(new ClassPatternDetector(), model);
		case "DOT-Generation":
			DotGeneratorPhase dotPhase = new DotGeneratorPhase(model, properties);
			return dotPhase;
		default:
			throw new UnsupportedOperationException("Unknown phase type "+phaseType);
		}
		
	}
	
	private int getIntProperty(Properties properties, String key, int defaultValue) throws IOException {
		try {
			return Integer.parseInt(properties.getProperty(key, Integer.toString(defaultValue)));
		}
		catch (NumberFormatException ex) {
			throw new IOException(key + " must be a number");
		}
	}

}
