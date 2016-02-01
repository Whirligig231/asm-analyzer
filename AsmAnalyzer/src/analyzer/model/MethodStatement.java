package analyzer.model;

import analyzer.visitor.common.IVisitor;

public class MethodStatement implements IMethodStatement {

	private IMethod method;

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

	@Override
	public StatementType getType() {
		return StatementType.CALL_METHOD;
	}

	@Override
	public IClass getOwner() {
		return this.method.getOwner();
	}

	@Override
	public IMethod getMethod() {
		return this.method;
	}

	@Override
	public void setMethod(IMethod method) {
		this.method = method;
	}

}
