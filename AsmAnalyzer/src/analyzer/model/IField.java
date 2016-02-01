package analyzer.model;

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
	
	public IClass getOwner();
	public void setOwner(IClass owner);

}
