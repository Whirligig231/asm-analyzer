package analyzer.model;

import analyzer.visitor.common.ITraverser;

public interface IRelation extends ITraverser {
	
	public IClass getFirstClass();
	public IClass getSecondClass();
	public RelationType getType();
	
	public void setFirstClass(IClass firstClass);
	public void setSecondClass(IClass secondClass);
	public void setType(RelationType type);

}
