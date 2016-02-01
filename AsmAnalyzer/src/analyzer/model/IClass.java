package analyzer.model;

import java.util.Collection;
import java.util.Iterator;

import analyzer.visitor.common.ITraverser;

public interface IClass extends ITraverser {
	
	public String getName();
	public AccessLevel getAccessLevel();
	
	public void setName(String name);
	public void setAccessLevel(AccessLevel accessLevel);
	
	public IModel getOwner();
	public void setOwner(IModel model);
	
	// Using iterators and add functionality to prevent content coupling
	
	public Iterator<IMethod> getMethodIterator();
	public void addMethod(IMethod method);
	public IMethod getMethod(String name, String desc);
	
	public Iterator<IField> getFieldIterator();
	public void addField(IField field);
	public IField getField(String name, String type);

}
