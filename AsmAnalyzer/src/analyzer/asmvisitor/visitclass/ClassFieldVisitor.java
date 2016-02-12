package analyzer.asmvisitor.visitclass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.Field;
import analyzer.model.IClass;
import analyzer.model.IClassModelHolder;
import analyzer.model.IField;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.Relation;
import analyzer.model.RelationType;

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
		
		IField field = new Field(this.getModel());
		
		if (signature != null) {
			// It's a generic type; find the internal type(s) instead
			Matcher m = Pattern.compile("L([^<;]+);").matcher(signature);
			while (m.find()) {
				this.addAssociate(m.group(1));
				//System.out.println("ADD PARAMETER TO "+name);
				field.addTypeParameter(ClassNameStandardizer.standardize(m.group(1)));
			}
		}
		else {
			this.addAssociate(type);
		}

		field.setName(name);
		field.setAccessLevel(AccessLevel.getFromOpcodes(access));
		field.setStatic((Opcodes.ACC_STATIC & access) > 0);
		field.setType(ClassNameStandardizer.standardize(type));
		field.setOwner(this.getClassModel());
		
		this.getClassModel().addField(field);

		return toDecorate;
	}

	private void addAssociate(String type) {
		IClass assocClass = this.model.getClass(ClassNameStandardizer.standardize(type));
		if (assocClass != null) {
			IRelation relation = new Relation(this.getClassModel(), assocClass, RelationType.ASSOCIATES, this.model);
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
