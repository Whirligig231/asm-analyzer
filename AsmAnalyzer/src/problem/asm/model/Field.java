package problem.asm.model;

public class Field implements IField {
	
	private String name;
	private AccessLevel accessLevel;
	private String type;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AccessLevel getAccessLevel() {
		return this.accessLevel;
	}

	@Override
	public String getType() {
		return this.type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public void setType(String type) {
		this.type = type;
	}

}
