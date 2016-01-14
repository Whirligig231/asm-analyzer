package problem.asm.model;

import java.util.Collection;
import java.util.Iterator;

import problem.asm.visitor.ITraverser;

public interface IClass extends ITraverser {
	
	public String getName();
	public AccessLevel getAccessLevel();
	
	public void setName(String name);
	public void setAccessLevel(AccessLevel accessLevel);
	
	// Using iterators and add functionality to prevent content coupling
	
	public Iterator<IMethod> getMethodIterator();
	public void addMethod(IMethod method);
	
	public Iterator<IField> getFieldIterator();
	public void addField(IField field);

}
