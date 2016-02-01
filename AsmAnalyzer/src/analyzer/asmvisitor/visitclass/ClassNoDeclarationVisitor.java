package analyzer.asmvisitor.visitclass;

import org.objectweb.asm.ClassVisitor;

public class ClassNoDeclarationVisitor extends ClassVisitor {

	public ClassNoDeclarationVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){

		// Nothing
		
	}

}
