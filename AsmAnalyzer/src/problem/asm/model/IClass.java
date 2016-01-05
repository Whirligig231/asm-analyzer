package problem.asm.model;

public interface IClass {
	
	public String getName();
	public AccessLevel getAccessLevel();
	public String getSuperClass();
	public String[] getInterfaces();

}
