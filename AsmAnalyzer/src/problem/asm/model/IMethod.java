package problem.asm.model;

import java.util.Iterator;
import java.util.ListIterator;

import problem.asm.visitor.ITraverser;

public interface IMethod extends ITraverser {
	
	public IClass getOwner();
	public String getName();
	public String getDesc();
	public AccessLevel getAccessLevel();
	public boolean isStatic();
	public String getReturnType();
	public String[] getArgTypes();
	
	public void setOwner(IClass owner);
	public void setName(String name);
	public void setDesc(String desc);
	public void setAccessLevel(AccessLevel accessLevel);
	public void setStatic(boolean isStatic);
	public void setReturnType(String returnType);
	public void setArgTypes(String[] argTypes);
	
	public void addStatement(IStatement stat);
	public ListIterator<IStatement> getStatementIterator();

}
