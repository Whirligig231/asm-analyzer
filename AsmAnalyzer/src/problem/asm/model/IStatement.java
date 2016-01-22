package problem.asm.model;

import problem.asm.visitor.ITraverser;

public interface IStatement extends ITraverser {
	
	public StatementType getType();
	
	public IClass getOwner();

}
