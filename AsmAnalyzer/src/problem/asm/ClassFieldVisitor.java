package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

public class ClassFieldVisitor extends ClassVisitor{

	public ClassFieldVisitor(int api){
		super(api);
	}
	
	public ClassFieldVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);
		// TODO Auto-generated constructor stub
	}
	
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);
		String type = Type.getType(desc).getClassName();
		// TODO: delete this line
		System.out.println("	"+type+" "+ name);
		// TODO: add this field to your internal representation of the current class.
		// What is a good way to know what the current class is?
		return toDecorate;
	};

}
