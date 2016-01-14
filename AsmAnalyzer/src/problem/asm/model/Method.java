package problem.asm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import problem.asm.visitor.IVisitor;

public class Method implements IMethod {
	
	private String name;
	private String desc;

	private AccessLevel accessLevel;
	private String returnType;
	private String[] argTypes;
	
	private List<IMethod> calls;
	
	public Method() {
		this.calls = new ArrayList<>();
	}

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
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

	@Override
	public void addCall(IMethod dest) {
		this.calls.add(dest);
	}

	@Override
	public ListIterator<IMethod> getCallIterator() {
		return Collections.unmodifiableList(this.calls).listIterator();
	}

}
