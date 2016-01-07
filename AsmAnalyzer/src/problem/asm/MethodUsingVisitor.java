package problem.asm;

import org.objectweb.asm.MethodVisitor;

import problem.asm.model.IClass;
import problem.asm.model.IClassHolder;

public class MethodUsingVisitor extends MethodVisitor implements IClassHolder {
	
	private IClass classModel;

	public MethodUsingVisitor(int api) {
		super(api);
		// TODO Auto-generated constructor stub
	}

	public MethodUsingVisitor(int api, MethodVisitor mv) {
		super(api, mv);
		// TODO Auto-generated constructor stub
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
		this.processUse(owner);
	}
	
	private void processUse(String classname) {
		this.classModel.addUse(classname);
	}

	@Override
	public IClass getClassModel() {
		return this.classModel;
	}

	public void setClassModel(IClass classModel) {
		this.classModel = classModel;
	}

}
