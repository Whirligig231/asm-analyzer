package problem.asm.visitor;

import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;

public interface IVisitor {
	public void preVisit(IClass c);
	public void postFieldsVisit(IClass c);
	public void postMethodsVisit(IClass c);

	public void visit(IField b);

	public void visit(IMethod e);
	public void visit(IRelation relation);
	public void preVisit(IModel model);
	public void postVisit(IModel model);
}
