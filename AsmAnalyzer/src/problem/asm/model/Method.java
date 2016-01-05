package problem.asm.model;

import problem.asm.visitor.IVisitor;

public class Method implements IMethod {
	
	private String name;
	private AccessLevel accessLevel;
	private String returnType;
	private String[] argTypes;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AccessLevel getAccessLevel() {
		return this.accessLevel;
	}

	@Override
	public String getReturnType() {
		return this.returnType;
	}

	@Override
	public String[] getArgTypes() {
		return this.argTypes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public void setArgTypes(String[] argTypes) {
		this.argTypes = argTypes;
	}

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

}
