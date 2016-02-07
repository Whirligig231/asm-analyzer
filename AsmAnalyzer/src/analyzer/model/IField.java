package analyzer.model;

import java.util.Iterator;

import analyzer.visitor.common.ITraverser;

public interface IField extends ITraverser {
	
	public String getName();
	public AccessLevel getAccessLevel();
	public boolean isStatic();
	public String getType();
	
	public void setName(String name);
	public void setAccessLevel(AccessLevel accessLevel);
	public void setStatic(boolean isStatic);
	public void setType(String type);
	
	public void addTypeParameter(String type);
	public Iterator<String> getTypeParameterIterator();
	
	public IClass getOwner();
	public void setOwner(IClass owner);

}
