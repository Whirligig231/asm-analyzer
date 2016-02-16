package analyzer.pipeline;

import java.io.IOException;
import java.util.Properties;

import analyzer.model.IModel;

public interface IPhaseFactory {
	
	public IPhase makePhase(String phaseType, IModel model, Properties properties) throws IOException;

}
