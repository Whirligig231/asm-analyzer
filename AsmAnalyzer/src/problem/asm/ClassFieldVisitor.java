package problem.asm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

import problem.asm.model.IClass;
import problem.asm.model.IClassModelHolder;
import problem.asm.model.IField;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.Relation;
import problem.asm.model.RelationType;
import problem.asm.model.AccessLevel;
import problem.asm.model.Class;
import problem.asm.model.Field;

public class ClassFieldVisitor extends ClassVisitor implements IClassModelHolder {
	
	private IClassModelHolder holder;
	private IModel model;

	public ClassFieldVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);

		if (!(decorated instanceof IClassModelHolder))
			throw new UnsupportedOperationException("Must decorate an IClassModelHolder visitor!");
		else {
			IClassModelHolder classHolder = (IClassModelHolder)decorated;
			this.holder = classHolder;
			this.model = classHolder.getModel();
		}
	}
	
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);

		String type = Type.getType(desc).getClassName();
		
		if (signature != null) {
			// It's a generic type; find the internal type(s) instead
			Matcher m = Pattern.compile("L([^<;]+);").matcher(signature);
			while (m.find()) {
				this.addAssociate(m.group(1));
			}
		}
		else {
			this.addAssociate(type);
		}

		IField field = new Field();
		field.setName(name);
		field.setAccessLevel(AccessLevel.getFromOpcodes(access));
		field.setType(type);
		field.setOwner(this.getClassModel());
		
		this.getClassModel().addField(field);

		return toDecorate;
	}

	private void addAssociate(String type) {
		IClass assocClass = this.model.getClass(ClassNameStandardizer.standardize(type));
		if (assocClass != null) {
			IRelation relation = new Relation(this.getClassModel(), assocClass, RelationType.ASSOCIATES);
			this.model.addRelation(relation);
		}
	}

	@Override
	public IClass getClassModel() {
		return this.holder.getClassModel();
	}

	@Override
	public IModel getModel() {
		return this.model;
	};

}
