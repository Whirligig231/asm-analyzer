package problem.asm;

import org.objectweb.asm.MethodVisitor;

import problem.asm.model.IClass;
import problem.asm.model.Class;
import problem.asm.model.IClassModelHolder;
import problem.asm.model.IMethod;
import problem.asm.model.IMethodHolder;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.Method;
import problem.asm.model.Relation;
import problem.asm.model.RelationType;

public class MethodCallsVisitor extends MethodVisitor implements IClassModelHolder, IMethodHolder {
	
	private IModel model;
	private IClass classModel;
	private IMethod method;
	private SequenceGenerator sg;
	private int level;

	public MethodCallsVisitor(int api, MethodVisitor toDecorate, ClassMethodVisitor classMethodVisitor,
			SequenceGenerator sg, int level) {
		super(api, toDecorate);
		this.model = classMethodVisitor.getModel();
		this.classModel = classMethodVisitor.getClassModel();
		this.method = classMethodVisitor.getMethod();
		this.sg = sg;
		this.level = level;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);
		
		IClass destClass = this.model.getClass(ClassNameStandardizer.standardize(owner));
		if (destClass == null) {
			destClass = new Class();
			destClass.setName(ClassNameStandardizer.standardize(owner));
			this.model.addClass(destClass);
		}
		
		IMethod destMethod = destClass.getMethod(name, desc);
		if (destMethod == null) {
			destMethod = new Method();
			destMethod.setOwner(destClass);
			destMethod.setName(name);
			destMethod.setDesc(desc);
			destClass.addMethod(destMethod);
		}
		
		// TODO: remove this debug line at some point
		// System.out.println(this.method.getName() + " calls "+destMethod.getName());
		
		this.method.addCall(destMethod);
		
		if (this.level > 0)
			this.sg.addMethod(ClassNameStandardizer.standardize(owner), name, desc, this.level - 1);
		
	}

	@Override
	public IClass getClassModel() {
		return this.classModel;
	}

	@Override
	public IModel getModel() {
		return this.model;
	}

	@Override
	public IMethod getMethod() {
		return this.method;
	}

}
