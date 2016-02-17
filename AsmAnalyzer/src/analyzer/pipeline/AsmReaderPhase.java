package analyzer.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

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
	private Properties properties;

	public AsmReaderPhase(IModel model, Properties properties) {
		this.model = model;
		this.properties = properties;
	}

	@Override
	public void run() throws IOException {
		
		String classes = properties.getProperty("Input-Classes");
		if (classes == null) {
			throw new IOException("Input-Classes must be set");
		}
		String[] classNames = classes.split("[;, ]");
		
		/*for (String className: classNames) {

			if (model.getClass(ClassNameStandardizer.standardize(className)) == null)
				continue;
			
			IClass classModel = new Class();
			classModel.setName(ClassNameStandardizer.standardize(className));
			classModel.setOwner(model);
			model.addClass(classModel);

		}*/
		
		String folder = properties.getProperty("Input-Folder");
		
		Collection<String> newClassNames = new ArrayList<>(Arrays.asList(classNames));
		
		if (folder != null)
			this.readClasses(new File(folder), newClassNames);
		
		for (String className: classNames) {

			// ASM's ClassReader does the heavy lifting of parsing the compiled Java class
			ClassReader reader=new ClassReader(className);
			this.readClass(reader);

		}
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String className : newClassNames) {
			if (!first)
				sb.append(";");
			sb.append(className);
			first = false;
		}
		
		properties.setProperty("Input-Classes", sb.toString());
	}

	@Override
	public void update(Observable o, Object arg) {
//		System.out.println("Update in AsmReaderPhase");
//		System.out.println("Notify observers from AsmReaderPhase");
		this.setChanged();
		this.notifyObservers(arg);
	}
	
	private void readClass(ClassReader reader) {
		
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
		
//					System.out.println("ACCEPT "+className);
		
		// Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
		reader.accept(notifyVisitor, ClassReader.EXPAND_FRAMES);
		
	}
	
	private void readClasses(File file, Collection<String> newClassNames)
			throws FileNotFoundException, IOException {
		
		if (file.isDirectory()) {
			for (File item : file.listFiles())
				this.readClasses(item, newClassNames);
		}
		else if (file.getName().endsWith(".class")) {
			ClassReader reader = new ClassReader(new FileInputStream(file));
			newClassNames.add(reader.getClassName());
			this.readClass(reader);
		}
		
	}

}
