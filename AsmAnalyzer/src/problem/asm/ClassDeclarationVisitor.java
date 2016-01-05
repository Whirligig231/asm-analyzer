package problem.asm;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;

public class ClassDeclarationVisitor extends ClassVisitor {
	public ClassDeclarationVisitor(int api){
		super(api);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
		// TODO: delete the line below
		System.out.println("Class: "+name+" extends "+superName+" implements "+Arrays.toString(interfaces));
		// TODO: construct an internal representation of the class for later use by decorators
		super.visit(version, access, name, signature, superName, interfaces);
		
	}
}
