package problem.asm.model;

import problem.asm.visitor.IVisitor;

public class Relation implements IRelation {

	private IClass firstClass;
	private IClass secondClass;
	private RelationType type;
	
	public Relation(IClass firstClass, IClass secondClass, RelationType type) {
		this.firstClass = firstClass;
		this.secondClass = secondClass;
		this.type = type;
	}

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

	@Override
	public IClass getFirstClass() {
		return this.firstClass;
	}

	@Override
	public IClass getSecondClass() {
		return this.secondClass;
	}

	@Override
	public RelationType getType() {
		return this.type;
	}

	@Override
	public void setFirstClass(IClass firstClass) {
		this.firstClass = firstClass;
	}

	@Override
	public void setSecondClass(IClass secondClass) {
		this.secondClass = secondClass;
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
		if (getClass() != obj.getClass())
			return false;
		Relation other = (Relation) obj;
		if (firstClass == null) {
			if (other.firstClass != null)
				return false;
		} else if (!firstClass.equals(other.firstClass))
			return false;
		if (secondClass == null) {
			if (other.secondClass != null)
				return false;
		} else if (!secondClass.equals(other.secondClass))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
