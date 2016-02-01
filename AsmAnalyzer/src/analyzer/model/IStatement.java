package analyzer.model;

import analyzer.visitor.common.ITraverser;

public interface IStatement extends ITraverser {
	
	public StatementType getType();
	
	public IClass getOwner();

}
