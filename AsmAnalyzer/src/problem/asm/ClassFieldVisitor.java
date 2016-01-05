package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

import problem.asm.model.IClass;
import problem.asm.model.IClassHolder;
import problem.asm.model.Class;

public class ClassFieldVisitor extends ClassVisitor implements IClassHolder {
	
	private IClass classModel;

	public ClassFieldVisitor(int api){
		super(api);
		this.classModel = new Class();
	}
	
	public ClassFieldVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);

		if (!(decorated instanceof IClassHolder))
			throw new UnsupportedOperationException("Must decorate an IClassHolder visitor!");
		else {
			IClassHolder classHolder = (IClassHolder)decorated;
			this.classModel = classHolder.getClassModel();
		}
	}
	
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);
		String type = Type.getType(desc).getClassName();
		// TODO: delete this line
		System.out.println("	"+type+" "+ name);
		// TODO: add this field to your internal representation of the current class.
		// What is a good way to know what the current class is?
		return toDecorate;
	}

	@Override
	public IClass getClassModel() {
		return this.classModel;
	};

}
