package problem.asm.pattern;

import java.awt.Color;
import java.util.Collection;

import problem.asm.model.IClass;
import problem.asm.visitor.ITraverser;

public interface IPattern extends ITraverser {
	public Collection<IClass> getClasses();
	public Color getColor();
	public String getName();
	public void addClass(IClass clazz);
	public void removeClass(IClass clazz);
}
