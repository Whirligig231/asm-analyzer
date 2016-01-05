package problem.asm;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;

import problem.asm.model.IClass;
import problem.asm.model.AccessLevel;
import problem.asm.model.Class;
import problem.asm.model.IClassHolder;

public class ClassDeclarationVisitor extends ClassVisitor implements IClassHolder {
	
	private IClass classModel;
	
	public ClassDeclarationVisitor(int api){
		super(api);
		this.classModel = new Class();
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){

		super.visit(version, access, name, signature, superName, interfaces);
		
		this.classModel.setName(name);
		this.classModel.setAccessLevel(AccessLevel.getFromOpcodes(access));
		this.classModel.setSuperClass(superName);
		this.classModel.setInterfaces(interfaces);
		
	}

	@Override
	public IClass getClassModel() {
		return this.classModel;
	}
}
