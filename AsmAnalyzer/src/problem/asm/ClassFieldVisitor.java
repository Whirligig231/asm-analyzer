package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

import problem.asm.model.IClass;
import problem.asm.model.IClassHolder;
import problem.asm.model.IField;
import problem.asm.model.AccessLevel;
import problem.asm.model.Class;
import problem.asm.model.Field;

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

		IField field = new Field();
		field.setName(name);
		field.setAccessLevel(AccessLevel.getFromOpcodes(access));
		field.setType(type);
		
		this.classModel.addField(field);

		return toDecorate;
	}

	@Override
	public IClass getClassModel() {
		return this.classModel;
	};

}
