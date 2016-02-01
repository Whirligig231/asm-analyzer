package analyzer.model;

import analyzer.visitor.common.IVisitor;

public class InstantiationStatement implements IInstantiationStatement {

	private IClass owner;

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

	@Override
	public StatementType getType() {
		return StatementType.INSTANTIATE;
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
