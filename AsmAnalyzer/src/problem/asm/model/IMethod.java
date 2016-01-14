package problem.asm.model;

import java.util.Iterator;
import java.util.ListIterator;

import problem.asm.visitor.ITraverser;

public interface IMethod extends ITraverser {
	
	public String getName();
	public String getDesc();
	public AccessLevel getAccessLevel();
	public String getReturnType();
	public String[] getArgTypes();
	
	public void setName(String name);
	public void setDesc(String desc);
	public void setAccessLevel(AccessLevel accessLevel);
	public void setReturnType(String returnType);
	public void setArgTypes(String[] argTypes);
	
	public void addCall(IMethod dest);
	public ListIterator<IMethod> getCallIterator();

}
