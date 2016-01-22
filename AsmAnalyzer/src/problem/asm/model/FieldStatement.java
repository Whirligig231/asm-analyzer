package problem.asm.model;

import problem.asm.visitor.IVisitor;

public class FieldStatement implements IFieldStatement {

	private StatementType type;
	private IClass owner;
	private IField field;

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

	@Override
	public StatementType getType() {
		return this.type;
	}

	@Override
	public IClass getOwner() {
		return this.field.getOwner();
	}

	@Override
	public void setType(StatementType type) {
		this.type = type;
	}

	@Override
	public IField getField() {
		return this.field;
	}

	@Override
	public void setField(IField field) {
		this.field = field;
	}

}
