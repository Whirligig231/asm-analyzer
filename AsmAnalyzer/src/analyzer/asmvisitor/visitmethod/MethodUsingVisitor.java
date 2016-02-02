package analyzer.asmvisitor.visitmethod;

import org.objectweb.asm.MethodVisitor;

import analyzer.asmvisitor.visitclass.ClassMethodVisitor;
import analyzer.common.ClassNameStandardizer;
import analyzer.model.IClass;
import analyzer.model.IClassModelHolder;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.Relation;
import analyzer.model.RelationType;

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
			IRelation relation = new Relation(this.classModel, useClass, RelationType.USES, this.model);
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
