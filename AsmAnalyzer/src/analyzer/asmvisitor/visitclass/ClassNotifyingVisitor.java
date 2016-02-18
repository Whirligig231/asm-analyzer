package analyzer.asmvisitor.visitclass;

import java.util.Observer;

import org.objectweb.asm.ClassVisitor;

public class ClassNotifyingVisitor extends ClassVisitor {
	
	private final ClassInputObservable observable;

	public ClassNotifyingVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);
		this.observable = new ClassInputObservable();
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){

		super.visit(version, access, name, signature, superName, interfaces);
		
//		System.out.println("Visiting in ClassNotifyingVisitor");
		this.observable.classVisitUpdate(name);
		
	}

	public void addObserver(Observer observer) {
		this.observable.addObserver(observer);
	}

}
