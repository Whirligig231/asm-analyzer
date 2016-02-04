package analyzer.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import analyzer.asmvisitor.visitclass.ClassDeclarationVisitor;
import analyzer.asmvisitor.visitclass.ClassFieldVisitor;
import analyzer.asmvisitor.visitclass.ClassMethodVisitor;
import analyzer.asmvisitor.visitclass.ClassStatementsVisitor;
import analyzer.common.ClassNameStandardizer;
import analyzer.model.Class;
import analyzer.model.IClass;
import analyzer.model.IModel;
import analyzer.model.Model;
import analyzer.model.pattern.IAnnotatedClass;
import analyzer.model.pattern.SingletonClass;
import analyzer.visitor.detect.AdapterDetector;
import analyzer.visitor.detect.DecoratorDetector;
import analyzer.visitor.detect.DecoratorSubclassDetector;
import analyzer.visitor.detect.SingletonDetector;

public class SingletonTest {
	ClassDeclarationVisitor cdv;
	IModel model;
	Map<String, IClass> classMap = new HashMap<String, IClass>();
	Map<String, Boolean> isSingletonMap = new HashMap<String, Boolean>();

	@Before
	public void setUp() throws Exception {
		model = new Model();
		
		isSingletonMap.put("java.lang.Runtime", false);
		isSingletonMap.put("java.awt.Desktop", false);
		isSingletonMap.put("java.util.Calendar", false);
		isSingletonMap.put("java.io.FilterInputStream", false);
		isSingletonMap.put("analyzer.tests.classes.EagerSingleton", false);
		
		for(String className: isSingletonMap.keySet()){
			//System.out.println("Loading class: "+className);
			IClass classModel = new Class();
			classModel.setName(ClassNameStandardizer.standardize(className));
			classModel.setOwner(model);
			model.addClass(classModel);
			classMap.put(className, classModel);
		}
		
		for(String className: isSingletonMap.keySet()){
			//System.out.println("Loading class: " + className);
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
			
			// Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
			reader.accept(statementsVisitor, ClassReader.EXPAND_FRAMES);

		}

		SingletonDetector sd = new SingletonDetector();
		sd.detect(model);
		
		AdapterDetector ad = new AdapterDetector();
		ad.detect(model);
		
		DecoratorDetector dd = new DecoratorDetector();
		dd.detect(model);
		
		// Propagates detection down to subclasses
		DecoratorSubclassDetector dsd = new DecoratorSubclassDetector();
		dsd.detect(model);
		
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public boolean hasSingletonPattern(IModel model, IClass classModel){
		Iterator<IClass> classIterator = model.getClassIterator();
		while (classIterator.hasNext()){
			IClass clazz = classIterator.next();
			if (clazz instanceof IAnnotatedClass) {
				IAnnotatedClass aClass = (IAnnotatedClass) clazz;
				System.out.println(aClass.getAnnotation());
				if (aClass.getAnnotation().equals("Singleton")) {
					return true;
				}
			}
		}
		return false;
	}

	@Test
	public void test() {
		for(String className: isSingletonMap.keySet()){
			IClass classModel = classMap.get(className);
			assertEquals(className + " is/isn't a singleton and should/shouldn't be!", isSingletonMap.get(className), hasSingletonPattern(model, classModel));
		}
	}

}
