package problem.asm.model;

import java.util.Collection;
import java.util.Iterator;

import problem.asm.pattern.IPattern;
import problem.asm.visitor.ITraverser;

public interface IModel extends ITraverser {
	
	public Iterator<IClass> getClassIterator();
	public void addClass(IClass classModel);
	public IClass getClass(String className);
	
	public Iterator<IRelation> getRelationIterator();
	public void addRelation(IRelation relation);
	
	public Collection<IPattern> getPatterns(IClass classModel);
	public void addPattern(IPattern pattern);

}
