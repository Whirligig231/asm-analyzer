package analyzer.pipeline;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import analyzer.asmvisitor.visitclass.ClassDeclarationVisitor;
import analyzer.asmvisitor.visitclass.ClassFieldVisitor;
import analyzer.asmvisitor.visitclass.ClassMethodVisitor;
import analyzer.asmvisitor.visitclass.ClassNotifyingVisitor;
import analyzer.asmvisitor.visitclass.ClassStatementsVisitor;
import analyzer.common.ClassNameStandardizer;
import analyzer.model.Class;
import analyzer.model.IClass;
import analyzer.model.IModel;

public class AsmReaderPhase extends Observable implements IPhase, Observer {
	
	private IModel model;
	private String[] classes;

	public AsmReaderPhase(IModel model) {
		this.model = model;
	}
	
	public String[] getClasses() {
		return classes;
	}

	public void setClasses(String[] classes) {
		this.classes = classes;
	}

	@Override
	public void run() throws IOException {
		
		for (String className: this.classes) {

			if (model.getClass(ClassNameStandardizer.standardize(className)) == null)
				continue;
			
			IClass classModel = new Class();
			classModel.setName(ClassNameStandardizer.standardize(className));
			classModel.setOwner(model);
			model.addClass(classModel);

		}
		
		for (String className: this.classes) {

			// ASM's ClassReader does the heavy lifting of parsing the compiled Java class
			ClassReader reader=new ClassReader(className);
			
			// make class declaration visitor to get superclass and interfaces
			ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, model);
			
			// DECORATE declaration visitor with field visitor
			ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor);
			
			// DECORATE field visitor with method visitor
			ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor);
			
			// DECORATE method visitor with statement visitor
			ClassVisitor statementsVisitor = new ClassStatementsVisitor(Opcodes.ASM5, methodVisitor);
			
			// DECORATE statement visitor with notifying visitor
			ClassNotifyingVisitor notifyVisitor = new ClassNotifyingVisitor(Opcodes.ASM5, statementsVisitor);
			notifyVisitor.addObserver(this);
			
			// Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
			reader.accept(notifyVisitor, ClassReader.EXPAND_FRAMES);

		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		this.notifyObservers(arg);
	}

}
