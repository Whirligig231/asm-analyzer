package analyzer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import analyzer.visitor.common.IVisitor;

public class Field implements IField {
	
	private String name;
	private AccessLevel accessLevel;
	private boolean isStatic;
	private String type;
	private IModel model;
	private String owner;
	private Collection<String> typeParameters;
	
	public Field(IModel model) {
		this.model = model;
		this.typeParameters = new ArrayList<String>();
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
		return this.model.getClass(this.owner);
	}

	@Override
	public void setOwner(IClass owner) {
		this.owner = owner.getName();
	}

	@Override
	public boolean isStatic() {
		return isStatic;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		if (accessLevel != other.accessLevel)
			return false;
		if (isStatic != other.isStatic)
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	@Override
	public void addTypeParameter(String type) {
		this.typeParameters.add(type);
	}

	@Override
	public Iterator<String> getTypeParameterIterator() {
		return this.typeParameters.iterator();
	}

}
