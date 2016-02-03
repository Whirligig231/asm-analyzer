package analyzer.model;

import analyzer.visitor.common.IVisitor;

public class Relation implements IRelation {

	private String firstClass;
	private String secondClass;
	private RelationType type;
	private IModel owner;
	
	public Relation(IClass firstClass, IClass secondClass, RelationType type, IModel owner) {
		this.firstClass = firstClass.getName();
		this.secondClass = secondClass.getName();
		this.type = type;
		this.owner = owner;
	}

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

	@Override
	public IClass getFirstClass() {
		return this.owner.getClass(this.firstClass);
	}

	@Override
	public IClass getSecondClass() {
		return this.owner.getClass(this.secondClass);
	}

	@Override
	public RelationType getType() {
		return this.type;
	}

	@Override
	public void setFirstClass(IClass firstClass) {
		this.firstClass = firstClass.getName();
	}

	@Override
	public void setSecondClass(IClass secondClass) {
		this.secondClass = secondClass.getName();
	}

	@Override
	public void setType(RelationType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstClass == null) ? 0 : firstClass.hashCode());
		result = prime * result + ((secondClass == null) ? 0 : secondClass.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IRelation))
			return false;
		IRelation other = (IRelation) obj;
		if (getFirstClass() == null) {
			if (other.getFirstClass() != null)
				return false;
		} else if (!getFirstClass().equals(other.getFirstClass()))
			return false;
		if (getSecondClass() == null) {
			if (other.getSecondClass() != null)
				return false;
		} else if (!getSecondClass().equals(other.getSecondClass()))
			return false;
		if (getType() != other.getType())
			return false;
		return true;
	}

}
