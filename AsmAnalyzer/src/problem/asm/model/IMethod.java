package problem.asm.model;

public interface IMethod {
	
	public String getName();
	public AccessLevel getAccessLevel();
	public String getReturnType();
	public String[] getArgTypes();
	
	public void setName(String name);
	public void setAccessLevel(AccessLevel accessLevel);
	public void setReturnType(String returnType);
	public void setArgTypes(String[] argTypes);

}
