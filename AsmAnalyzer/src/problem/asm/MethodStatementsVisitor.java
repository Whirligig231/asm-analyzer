package problem.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import problem.asm.model.IClass;
import problem.asm.model.AccessLevel;
import problem.asm.model.Class;
import problem.asm.model.Field;
import problem.asm.model.FieldStatement;
import problem.asm.model.IClassModelHolder;
import problem.asm.model.IField;
import problem.asm.model.IFieldStatement;
import problem.asm.model.IInstantiationStatement;
import problem.asm.model.IMethod;
import problem.asm.model.IMethodHolder;
import problem.asm.model.IMethodStatement;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.InstantiationStatement;
import problem.asm.model.Method;
import problem.asm.model.MethodStatement;
import problem.asm.model.Relation;
import problem.asm.model.RelationType;
import problem.asm.model.StatementType;

public class MethodStatementsVisitor extends MethodVisitor implements IClassModelHolder, IMethodHolder {
	
	private IModel model;
	private IClass classModel;
	private IMethod method;

	public MethodStatementsVisitor(int api, MethodVisitor toDecorate, ClassStatementsVisitor classStatementsVisitor) {
		super(api, toDecorate);
		this.model = classStatementsVisitor.getModel();
		this.classModel = classStatementsVisitor.getClassModel();
		this.method = classStatementsVisitor.getMethod();
	}
	
	@Override
	public void visitTypeInsn(int opcode, String type) {

		super.visitTypeInsn(opcode, type);
		
		if (opcode != Opcodes.NEW && opcode != Opcodes.NEWARRAY)
			return;

		IClass destClass = this.model.getClass(ClassNameStandardizer.standardize(type));
		if (destClass == null) {
			destClass = new Class();
			destClass.setName(ClassNameStandardizer.standardize(type));
			destClass.setOwner(this.model);
			this.model.addClass(destClass);
		}
		
		IInstantiationStatement mst = new InstantiationStatement();
		mst.setOwner(destClass);
		
		this.method.addStatement(mst);
		
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		super.visitFieldInsn(opcode, owner, name, desc);
		
		String type = Type.getType(desc).getClassName();
		
		IClass destClass = this.model.getClass(ClassNameStandardizer.standardize(owner));
		if (destClass == null) {
			destClass = new Class();
			destClass.setName(ClassNameStandardizer.standardize(owner));
			destClass.setOwner(this.model);
			this.model.addClass(destClass);
		}
		
		IField destField = destClass.getField(name, type);
		if (destField == null) {
			destField = new Field();
			destField.setOwner(destClass);
			destField.setName(name);
			destField.setType(type);
			
			destClass.addField(destField);
		}
		
		IFieldStatement mst = new FieldStatement();
		mst.setField(destField);
		
		if (opcode == Opcodes.PUTSTATIC || opcode == Opcodes.PUTFIELD)
			mst.setType(StatementType.SET_FIELD);
		else
			mst.setType(StatementType.GET_FIELD);
		
		this.method.addStatement(mst);
		
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);
		
		IClass destClass = this.model.getClass(ClassNameStandardizer.standardize(owner));
		if (destClass == null) {
			destClass = new Class();
			destClass.setName(ClassNameStandardizer.standardize(owner));
			destClass.setOwner(this.model);
			this.model.addClass(destClass);
		}
		
		IMethod destMethod = destClass.getMethod(name, desc);
		if (destMethod == null) {
			destMethod = new Method();
			destMethod.setOwner(destClass);
			destMethod.setName(name);
			destMethod.setDesc(desc);

			addReturnType(destMethod, desc);
			addArguments(destMethod, desc);
			
			destClass.addMethod(destMethod);
		}
		
		IMethodStatement mst = new MethodStatement();
		mst.setMethod(destMethod);
		
		this.method.addStatement(mst);
		
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
	
	void addReturnType(IMethod method, String desc){
		
		String returnType = Type.getReturnType(desc).getClassName();
		method.setReturnType(returnType);

	}
	
	void addArguments(IMethod method, String desc){
		Type[] args = Type.getArgumentTypes(desc);
		String[] argStrings = new String[args.length];
	    for (int i=0; i < args.length; i++){
	    	argStrings[i] = args[i].getClassName();
	    }
	    method.setArgTypes(argStrings);
	}

}