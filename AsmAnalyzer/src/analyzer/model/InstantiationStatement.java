package analyzer.model;

import analyzer.visitor.common.IVisitor;

public class InstantiationStatement implements IInstantiationStatement {

	private String owner;
	private IModel model;
	
	public InstantiationStatement(IModel model) {
		this.model = model;
	}

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
		return this.model.getClass(this.owner);
	}

	@Override
	public void setOwner(IClass owner) {
		this.owner = owner.getName();
	}

}
