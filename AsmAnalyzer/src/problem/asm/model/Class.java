package problem.asm.model;

public class Class implements IClass {
	
	private String name;
	private AccessLevel accessLevel;
	private String superClass;
	private String[] interfaces;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AccessLevel getAccessLevel() {
		return this.accessLevel;
	}

	@Override
	public String getSuperClass() {
		return this.superClass;
	}

	@Override
	public String[] getInterfaces() {
		return this.interfaces;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	public void setInterfaces(String[] interfaces) {
		this.interfaces = interfaces;
	}

}
