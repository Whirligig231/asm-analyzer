package problem.asm.visitor;

import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;

public abstract class VisitorAdapter implements IVisitor {

	@Override
	public void preVisit(IClass c) {}
	@Override
	public void postFieldsVisit(IClass c) {}
	@Override
	public void postMethodsVisit(IClass c) {}

	
	@Override
	public void visit(IField b) {}

	
	@Override
	public void visit(IMethod e) {}

}
