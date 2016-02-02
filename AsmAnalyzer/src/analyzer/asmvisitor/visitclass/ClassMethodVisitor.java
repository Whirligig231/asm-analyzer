package analyzer.asmvisitor.visitclass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import analyzer.asmvisitor.visitmethod.MethodUsingVisitor;
import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.IClass;
import analyzer.model.IClassModelHolder;
import analyzer.model.IMethod;
import analyzer.model.IMethodHolder;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.Method;
import analyzer.model.Relation;
import analyzer.model.RelationType;

public class ClassMethodVisitor extends ClassVisitor implements IClassModelHolder, IMethodHolder {
	
	private IClassModelHolder holder;
	
	private IModel model;
	private IMethod method;

	public ClassMethodVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);

		if (!(decorated instanceof IClassModelHolder))
			throw new UnsupportedOperationException("Must decorate an IClassHolder visitor!");
		else {
			IClassModelHolder classHolder = (IClassModelHolder)decorated;
			this.model = classHolder.getModel();
			this.holder = classHolder;
		}
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		MethodVisitor decorated = new MethodUsingVisitor(api, toDecorate, this);
		
		IMethod method = this.getClassModel().getMethod(name, desc);
		if (method == null) {
			method = new Method(this.getModel());
			method.setOwner(this.getClassModel());
			method.setName(name);
			method.setDesc(desc);

			addReturnType(method, desc);
			addArguments(method, desc);
			
			this.getClassModel().addMethod(method);
		}
		

		method.setStatic((Opcodes.ACC_STATIC & access) > 0);
		addAccessLevel(method, access);
		
		Matcher m = Pattern.compile("L([^<;]*);").matcher(desc);
		while (m.find()) {
			String useName = m.group(1);
			IClass useClass = this.model.getClass(ClassNameStandardizer.standardize(useName));
			if (useClass != null) {
				IRelation relation = new Relation(this.getClassModel(), useClass, RelationType.USES);
				this.model.addRelation(relation);
			}
		}

		this.method = method;

		return decorated;
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
		return this.holder.getClassModel();
	}

	@Override
	public IModel getModel() {
		return this.model;
	}

	@Override
	public IMethod getMethod() {
		return this.method;
	}
}
