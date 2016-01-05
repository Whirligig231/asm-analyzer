package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import problem.asm.model.IClass;
import problem.asm.model.IClassHolder;
import problem.asm.model.IMethod;
import problem.asm.model.Method;
import problem.asm.model.AccessLevel;
import problem.asm.model.Class;

public class ClassMethodVisitor extends ClassVisitor implements IClassHolder {
	
	private IClass classModel;
	
	public ClassMethodVisitor(int api){
		super(api);
		this.classModel = new Class();
	}
	
	public ClassMethodVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);

		if (!(decorated instanceof IClassHolder))
			throw new UnsupportedOperationException("Must decorate an IClassHolder visitor!");
		else {
			IClassHolder classHolder = (IClassHolder)decorated;
			this.classModel = classHolder.getClassModel();
		}
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);

		IMethod method = new Method();
		
		method.setName(name);
		addAccessLevel(method, access);
		addReturnType(method, desc);
		addArguments(method, desc);
		
	    this.classModel.addMethod(method);

		return toDecorate;
	}
	
	void addAccessLevel(IMethod method, int access){

		method.setAccessLevel(AccessLevel.getFromOpcodes(access));
		
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

	@Override
	public IClass getClassModel() {
		return this.classModel;
	}
}
