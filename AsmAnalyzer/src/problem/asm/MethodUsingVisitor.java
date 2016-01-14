package problem.asm;

import org.objectweb.asm.MethodVisitor;

import problem.asm.model.IClass;
import problem.asm.model.IClassModelHolder;
import problem.asm.model.IMethod;
import problem.asm.model.IMethodHolder;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.Relation;
import problem.asm.model.RelationType;

public class MethodUsingVisitor extends MethodVisitor implements IClassModelHolder{
	
	private IModel model;
	private IClass classModel;

	public MethodUsingVisitor(int api, MethodVisitor toDecorate, ClassMethodVisitor classMethodVisitor) {
		super(api, toDecorate);
		this.model = classMethodVisitor.getModel();
		this.classModel = classMethodVisitor.getClassModel();
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		super.visitTypeInsn(opcode, type);
		this.processUse(type);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		super.visitFieldInsn(opcode, owner, name, desc);
		this.processUse(owner);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);
		if (!name.equals("<init>"))
			this.processUse(owner);
	}
	
	private void processUse(String classname) {
		IClass useClass = this.model.getClass(ClassNameStandardizer.standardize(classname));
		if (useClass != null) {
			IRelation relation = new Relation(this.classModel, useClass, RelationType.USES);
			this.model.addRelation(relation);
		}
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
