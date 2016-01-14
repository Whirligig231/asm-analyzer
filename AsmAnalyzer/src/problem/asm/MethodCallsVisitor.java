package problem.asm;

import org.objectweb.asm.MethodVisitor;

import problem.asm.model.IClass;
import problem.asm.model.Class;
import problem.asm.model.IClassModelHolder;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.Method;
import problem.asm.model.Relation;
import problem.asm.model.RelationType;

public class MethodCallsVisitor extends MethodVisitor implements IClassModelHolder {
	
	private IModel model;
	private IClass classModel;
	private SequenceGenerator sg;
	private int level;

	public MethodCallsVisitor(int api, MethodVisitor toDecorate, ClassMethodVisitor classMethodVisitor,
			SequenceGenerator sg, int level) {
		super(api, toDecorate);
		this.model = classMethodVisitor.getModel();
		this.classModel = classMethodVisitor.getClassModel();
		this.sg = sg;
		this.level = level;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);
		
		IClass destClass = this.model.getClass(owner);
		if (destClass == null) {
			destClass = new Class();
			destClass.setName(owner);
			this.model.addClass(destClass);
		}
		
		IMethod destMethod = destClass.getMethod(name, desc);
		if (destMethod == null) {
			destMethod = new Method();
			destMethod.setName(name);
			destMethod.setDesc(desc);
			destClass.addMethod(destMethod);
		}
		
		// TODO: Add the call to the method
		
		this.sg.addMethod(owner, name, desc, this.level - 1);
	}

	@Override
	public IClass getClassModel() {
		return this.classModel;
	}

	@Override
	public IModel getModel() {
		return this.model;
	}

}
