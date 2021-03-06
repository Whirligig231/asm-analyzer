package analyzer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import analyzer.visitor.common.IVisitor;

public class Method implements IMethod {
	
	private IModel model;
	
	private String owner;
	private String name;
	private String desc;

	private AccessLevel accessLevel;
	private boolean isStatic;
	private String returnType;
	private String[] argTypes;
	
	private List<IStatement> statements;
	
	public Method(IModel model) {
		this.statements = new ArrayList<>();
		this.model = model;
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
		v.preVisit(this);
		for (IStatement stat : this.statements)
			stat.accept(v);
		v.postVisit(this);
	}

	@Override
	public IClass getOwner() {
		return this.model.getClass(this.owner);
	}

	@Override
	public void setOwner(IClass owner) {
		this.owner = owner.getName();
	}

	@Override
	public void addStatement(IStatement stat) {
		this.statements.add(stat);
	}

	@Override
	public ListIterator<IStatement> getStatementIterator() {
		return this.statements.listIterator();
	}

	@Override
	public boolean isStatic() {
		return this.isStatic;
	}

	@Override
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

}
