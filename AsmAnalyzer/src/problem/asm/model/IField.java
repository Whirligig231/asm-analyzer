package problem.asm.model;

public interface IField {
	
	public String getName();
	public AccessLevel getAccessLevel();
	public String getType();
	
	public void setName(String name);
	public void setAccessLevel(AccessLevel accessLevel);
	public void setType(String type);

}
