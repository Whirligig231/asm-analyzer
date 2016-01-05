package problem.asm.model;

public interface IMethod {
	
	public String getName();
	public AccessLevel getAccessLevel();
	public String getReturnType();
	public String[] getArgTypes();

}
