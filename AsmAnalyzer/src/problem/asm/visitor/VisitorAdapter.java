package problem.asm.visitor;

import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;

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
	
	@Override
	public void visit(IRelation relation) {}
	@Override
	public void preVisit(IModel model) {}
	@Override
	public void postVisit(IModel model) {}

}
