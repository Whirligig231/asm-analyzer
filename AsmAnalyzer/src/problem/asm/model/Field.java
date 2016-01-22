package problem.asm.model;

import problem.asm.visitor.IVisitor;

public class Field implements IField {
	
	private String name;
	private AccessLevel accessLevel;
	private boolean isStatic;
	private String type;
	private IClass owner;

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

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

	@Override
	public IClass getOwner() {
		return this.owner;
	}

	@Override
	public void setOwner(IClass owner) {
		this.owner = owner;
	}

	@Override
	public boolean isStatic() {
		return isStatic;
	}

	@Override
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

}
