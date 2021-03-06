package analyzer.asmvisitor.visitclass;

import org.objectweb.asm.ClassVisitor;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.Class;
import analyzer.model.IClass;
import analyzer.model.IClassModelHolder;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.Relation;
import analyzer.model.RelationType;

public class ClassDeclarationVisitor extends ClassVisitor implements IClassModelHolder {
	
	private IModel model;
	private IClass classModel;
	
	public ClassDeclarationVisitor(int api, IModel model){
		super(api);
		this.model = model;
		this.classModel = null;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
		//System.out.println("version: "+version+" access: "+access+" name: "+name+" signature: "+signature+" superName: "+superName+" interfaces: "+ interfaces);

		super.visit(version, access, name, signature, superName, interfaces);
		
		this.classModel = this.model.getClass(ClassNameStandardizer.standardize(name));
		if (this.classModel == null) {
			this.classModel = new Class();
			this.classModel.setName(ClassNameStandardizer.standardize(name));
			this.classModel.setOwner(model);
			this.model.addClass(this.classModel);
		}
		this.classModel.setAccessLevel(AccessLevel.getFromOpcodes(access));
		
		// Add super class relation
		IClass superClass = this.model.getClass(ClassNameStandardizer.standardize(superName));
		if (superClass != null) {
			IRelation relation = new Relation(this.classModel, superClass, RelationType.EXTENDS, this.model);
			this.model.addRelation(relation);
			
			this.classModel.setSuperClass(superClass);
		}
		
		// Add interface relations
		for (String inter : interfaces) {
			IClass superInter = this.model.getClass(ClassNameStandardizer.standardize(inter));
			if (superInter != null) {
				IRelation relation = new Relation(this.classModel, superInter, RelationType.IMPLEMENTS, this.model);
				this.model.addRelation(relation);
				
				this.classModel.addInterface(superInter);
			}
		}
		
	}

	@Override
	public IClass getClassModel() {
		return this.classModel;
	}

	@Override
	public IModel getModel() {
		return this.model;
	}
}
