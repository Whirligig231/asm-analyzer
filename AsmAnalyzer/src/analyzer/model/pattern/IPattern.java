package analyzer.model.pattern;

import java.awt.Color;
import java.util.Collection;

import analyzer.model.IClass;
import analyzer.visitor.common.ITraverser;

public interface IPattern extends ITraverser {
	public Collection<IClass> getClasses();
	public Color getColor();
	public String getName();
	public void addClass(IClass clazz);
	public void removeClass(IClass clazz);
}
