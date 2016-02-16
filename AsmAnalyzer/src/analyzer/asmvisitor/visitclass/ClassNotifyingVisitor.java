package analyzer.asmvisitor.visitclass;

import java.util.Observer;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import analyzer.asmvisitor.visitmethod.MethodStatementsVisitor;
import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.Class;
import analyzer.model.IClass;
import analyzer.model.IClassModelHolder;
import analyzer.model.IMethod;
import analyzer.model.IMethodHolder;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.Relation;
import analyzer.model.RelationType;

public class ClassNotifyingVisitor extends ClassVisitor {
	
	private final ClassInputObservable observable;

	public ClassNotifyingVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);
		this.observable = new ClassInputObservable();
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){

		super.visit(version, access, name, signature, superName, interfaces);
		
		this.observable.classVisitUpdate(name);
		
	}

	public void addObserver(Observer observer) {
		this.observable.addObserver(observer);
	}

}
