package analyzer.asmvisitor.visitclass;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import analyzer.asmvisitor.visitmethod.MethodStatementsVisitor;
import analyzer.model.IClass;
import analyzer.model.IClassModelHolder;
import analyzer.model.IMethod;
import analyzer.model.IMethodHolder;
import analyzer.model.IModel;

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
