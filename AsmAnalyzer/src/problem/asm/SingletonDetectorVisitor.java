package problem.asm;

import java.util.Iterator;

import org.objectweb.asm.ClassVisitor;

import problem.asm.model.IClass;
import problem.asm.model.IClassModelHolder;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;

public class SingletonDetectorVisitor extends ClassVisitor implements IClassModelHolder {
	
	private IModel model;
	private IClass classModel;
	
	public SingletonDetectorVisitor(int api, IModel model){
		super(api);
		this.model = model;
		this.classModel = null;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
		//System.out.println("version: "+version+" access: "+access+" name: "+name+" signature: "+signature+" superName: "+superName+" interfaces: "+ interfaces);

		super.visit(version, access, name, signature, superName, interfaces);
		
		this.classModel = this.model.getClass(ClassNameStandardizer.standardize(name));
		
		Iterator<IMethod> methodIterator = this.classModel.getMethodIterator();
		Iterator<IField> fieldIterator = this.classModel.getFieldIterator();
		
		try{
			while(methodIterator.hasNext()){
				IMethod method = methodIterator.next();
				
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