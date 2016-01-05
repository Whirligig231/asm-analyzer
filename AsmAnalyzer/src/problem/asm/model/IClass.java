package problem.asm.model;

import java.util.Iterator;

public interface IClass {
	
	public String getName();
	public AccessLevel getAccessLevel();
	public String getSuperClass();
	public String[] getInterfaces();
	
	// Using iterators and add functionality to prevent content coupling
	
	public Iterator<IMethod> getMethodIterator();
	public void addMethod(IMethod method);
	
	public Iterator<IField> getFieldIterator();
	public void addField(IField field);

}
