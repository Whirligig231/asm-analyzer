package problem.asm;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import problem.asm.model.IClass;
import problem.asm.model.IClassModelHolder;
import problem.asm.model.IMethod;
import problem.asm.model.IMethodHolder;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.Method;
import problem.asm.model.Relation;
import problem.asm.model.RelationType;
import problem.asm.model.AccessLevel;
import problem.asm.model.Class;

public class ClassCallsVisitor extends ClassVisitor implements IClassModelHolder, IMethodHolder {
	
	private ClassMethodVisitor decorated;
	private SequenceGenerator sg;
	private String name;

	public ClassCallsVisitor(int api, ClassMethodVisitor decorated, SequenceGenerator sg) {
		super(api, decorated);
		
		this.decorated = decorated;
		this.sg = sg;
		this.name = null;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
		super.visit(version, access, name, signature, superName, interfaces);		
		this.name = name;

		ClassVisitor dec = new ClassNoDeclarationVisitor(this.api, this);
		
		try {
			if (superName != null)
				(new ClassReader(superName)).accept(dec, this.api);
			for (String inter : interfaces)
				(new ClassReader(inter)).accept(dec, this.api);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		MethodVisitor toDecorate = this.decorated.visitMethod(access, name, desc, signature, exceptions);
		MethodVisitor decorated = toDecorate;
		
		
		MethodSignature next = this.sg.getNextMethod();
		if (next != null) {
			if (next.getName().equals(name) && desc.startsWith(next.getDesc())) {
				this.sg.popNextMethod();
				decorated = new MethodCallsVisitor(api, toDecorate, this.decorated,
						SequenceGenerator.getInstance(), next.getLevel());
			}
			else {
				// System.out.println("IT'S "+this.name+" "+name+" "+desc);
				// System.out.println("NOT "+next.getOwner()+" "+next.getName()+" "+next.getDesc());
			}
		}

		return decorated;
	}

	@Override
	public IClass getClassModel() {
		return this.decorated.getClassModel();
	}

	@Override
	public IModel getModel() {
		return this.decorated.getModel();
	}

	@Override
	public IMethod getMethod() {
		return this.decorated.getMethod();
	}
}
