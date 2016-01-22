package problem.asm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import problem.asm.model.AccessLevel;
import problem.asm.model.IClass;
import problem.asm.model.IClassModelHolder;
import problem.asm.model.IMethod;
import problem.asm.model.IMethodHolder;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.Method;
import problem.asm.model.Relation;
import problem.asm.model.RelationType;

public class ClassStatementsVisitor extends ClassVisitor implements IClassModelHolder, IMethodHolder {
	
	private IClassModelHolder holder;
	private IMethodHolder holderM;

	public ClassStatementsVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);

		if (!(decorated instanceof IClassModelHolder))
			throw new UnsupportedOperationException("Must decorate an IClassHolder visitor!");
		else {
			IClassModelHolder classHolder = (IClassModelHolder)decorated;
			this.holder = classHolder;
		}
		
		if (!(decorated instanceof IMethodHolder))
			throw new UnsupportedOperationException("Must decorate an IMethodHolder visitor!");
		else {
			IMethodHolder methodHolder = (IMethodHolder)decorated;
			this.holderM = methodHolder;
		}
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		MethodVisitor decorated = new MethodStatementsVisitor(api, toDecorate, this);
		return decorated;
	}

	@Override
	public IClass getClassModel() {
		return this.holder.getClassModel();
	}

	@Override
	public IModel getModel() {
		return this.holder.getModel();
	}

	@Override
	public IMethod getMethod() {
		return this.holderM.getMethod();
	}
}
