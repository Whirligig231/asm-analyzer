package problem.asm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import problem.asm.visitor.IVisitor;

public class Method implements IMethod {
	
	private IClass owner;
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
		// System.out.println("OWNER IS "+this.getOwner()+" "+this.getOwner().getName());
		// System.out.println("ADDING CALL TO "+this.getOwner()+" METHOD "+this.getName()+this.getDesc());
		this.calls.add(dest);
		// System.out.println("WE NOW HAVE "+this.calls.size()+" CALLS");
	}

	@Override
	public ListIterator<IMethod> getCallIterator() {
		// System.out.println("WE HAVE "+this.calls.size()+" CALLS");
		return Collections.unmodifiableList(this.calls).listIterator();
	}

	@Override
	public IClass getOwner() {
		return this.owner;
	}

	@Override
	public void setOwner(IClass owner) {
		this.owner = owner;
	}

}
