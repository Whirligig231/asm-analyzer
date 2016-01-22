package problem.asm.model;

import problem.asm.visitor.ITraverser;

public interface IField extends ITraverser {
	
	public String getName();
	public AccessLevel getAccessLevel();
	public String getType();
	
	public void setName(String name);
	public void setAccessLevel(AccessLevel accessLevel);
	public void setType(String type);
	
	public IClass getOwner();
	public void setOwner(IClass owner);

}
