package problem.asm.tests;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import problem.asm.ClassDeclarationVisitor;
import problem.asm.ClassFieldVisitor;
import problem.asm.ClassMethodVisitor;
import problem.asm.ClassNameStandardizer;
import problem.asm.ClassStatementsVisitor;
import problem.asm.SingletonDetector;
import problem.asm.model.Class;
import problem.asm.model.IClass;
import problem.asm.model.IModel;
import problem.asm.model.Model;
import problem.asm.pattern.IPattern;
import problem.asm.pattern.SingletonPattern;

public class SingletonTest {
	ClassDeclarationVisitor cdv;
	IModel model;
	Map<String, IClass> classMap = new HashMap<String, IClass>();
	Map<String, Boolean> isSingletonMap = new HashMap<String, Boolean>();

	@Before
	public void setUp() throws Exception {
		model = new Model();
		
		isSingletonMap.put("java.lang.Runtime", true);
		isSingletonMap.put("java.awt.Desktop", false);
		isSingletonMap.put("java.util.Calendar", false);
		isSingletonMap.put("java.io.FilterInputStream", false);
		isSingletonMap.put("problem.asm.tests.classes.EagerSingleton", true);
		
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

		SingletonDetector sd = SingletonDetector.getInstance();
		sd.detect(model);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public boolean hasSingletonPattern(IModel model, IClass classModel){
		Collection<IPattern> patterns = model.getPatterns(classModel);
		for(IPattern pattern : patterns){
			if (pattern instanceof SingletonPattern){
				return true;
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
