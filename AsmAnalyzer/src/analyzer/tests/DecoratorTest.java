package analyzer.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
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
import analyzer.visitor.detect.DecoratorPatternComponentDetector;
import analyzer.visitor.detect.DecoratorPatternSubclassDetector;

public class DecoratorTest {
	IModel model = new Model();
	Map<String, String> annotationClassMap = new HashMap<String, String>();
	@Before
	public void setUp() throws Exception {
		
		annotationClassMap.put("analyzer.tests.classes.ICat", "Component");
		annotationClassMap.put("analyzer.tests.classes.DecoratorCat", "Decorator");
		annotationClassMap.put("analyzer.tests.classes.TabbyCat", "Decorator");
		
		String[] classes = {"analyzer.tests.classes.ICat", "analyzer.tests.classes.DecoratorCat", "analyzer.tests.classes.TabbyCat", "analyzer.tests.classes.ConcreteCat"};
		
		for(String className: classes){
			//System.out.println("Loading class: "+className);
			IClass classModel = new Class();
			classModel.setName(ClassNameStandardizer.standardize(className));
			classModel.setOwner(model);
			model.addClass(classModel);
		}
		
		for(String className: classes){
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
		
		DecoratorPatternComponentDetector dd = new DecoratorPatternComponentDetector(3);
		dd.detect(model);
		
		// Propagates detection down to subclasses
		DecoratorPatternSubclassDetector dsd = new DecoratorPatternSubclassDetector();
		dsd.detect(model);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		for(String className : annotationClassMap.keySet()){
			String classNameStandardized = ClassNameStandardizer.standardize(className);
			IClass testClass = model.getClass(classNameStandardized);
			if (testClass == null)
				fail("Test class " + className + " was not in the model!");
			if (! (testClass instanceof IAnnotatedClass))
				fail("Test class " +className + " was not annotated!");
			
			IAnnotatedClass annotatedClass = (IAnnotatedClass) testClass;
			
			assertEquals(annotationClassMap.get(className), annotatedClass.getAnnotation());
		}
	}

}
