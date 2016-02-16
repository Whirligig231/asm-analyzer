package analyzer.model;

import java.util.Iterator;

import analyzer.model.pattern.IPattern;
import analyzer.visitor.common.ITraverser;

public interface IModel extends ITraverser {
	
	public Iterator<IClass> getClassIterator();
	public void addClass(IClass classModel);
	public IClass getClass(String className);
	
	public Iterator<IRelation> getRelationIterator();
	public void addRelation(IRelation relation);
	
	public Iterator<IPattern> getPatternIterator();
	public void addPattern(IPattern pattern);

}
