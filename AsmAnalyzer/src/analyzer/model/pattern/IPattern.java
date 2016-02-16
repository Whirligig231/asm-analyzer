package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;
import analyzer.visitor.common.ITraverser;

public interface IPattern extends Iterable<IClass>, ITraverser {
	
	public String getName();
	public Color getColor();
	
	public void addClass(IClass classModel);

}
