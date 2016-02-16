package analyzer.visitor.detect;

import analyzer.model.IModel;
import analyzer.pipeline.IPhase;

public interface IPatternDetector {
	public void detect(IModel model);
}
