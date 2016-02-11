package analyzer.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.After;
import org.junit.After;
import org.junit.Before;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import analyzer.asmvisitor.visitclass.ClassDeclarationVisitor;
import analyzer.asmvisitor.visitclass.ClassFieldVisitor;
import analyzer.asmvisitor.visitclass.ClassMethodVisitor;
import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.Class;
import analyzer.model.IClass;
import analyzer.model.IMethod;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.Model;
import analyzer.model.RelationType;

public class ClassMethodVisitorTest {
	IModel model;
	IClass carClass;
	IClass engineClass;

	@Before
	public void setUp() throws Exception {
		model = new Model();
		
		IClass classModel = new Class();
		classModel.setName(ClassNameStandardizer.standardize("problem.asm.tests.classes.Car"));
		model.addClass(classModel);
		

		IClass classModel2 = new Class();
		classModel2.setName(ClassNameStandardizer.standardize("problem.asm.tests.classes.Engine"));
		model.addClass(classModel2);

		IClass classModel3 = new Class();
		classModel3.setName(ClassNameStandardizer.standardize("problem.asm.tests.classes.Key"));
		model.addClass(classModel3);
		
		ClassDeclarationVisitor cdvCar = new ClassDeclarationVisitor(Opcodes.ASM5, model);
		ClassFieldVisitor cfvCar = new ClassFieldVisitor(Opcodes.ASM5, cdvCar);
		ClassMethodVisitor cmvCar = new ClassMethodVisitor(Opcodes.ASM5, cfvCar);
		ClassReader carReader =new ClassReader("problem.asm.tests.classes.Car");
		carReader.accept(cmvCar, ClassReader.EXPAND_FRAMES);
		carClass = cmvCar.getClassModel();
		
		ClassDeclarationVisitor cdvEngine = new ClassDeclarationVisitor(Opcodes.ASM5, model);
		ClassFieldVisitor cfvEngine = new ClassFieldVisitor(Opcodes.ASM5, cdvEngine);
		ClassMethodVisitor cmvEngine = new ClassMethodVisitor(Opcodes.ASM5, cfvEngine);
		ClassReader engineReader = new ClassReader("problem.asm.tests.classes.Engine");
		engineReader.accept(cmvEngine, ClassReader.EXPAND_FRAMES);
		engineClass = cmvEngine.getClassModel();
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void carMethodsTest() {
		Iterator<IMethod> methodIterator = carClass.getMethodIterator();
		IMethod startMethod = methodIterator.next();
		assertEquals(startMethod.getAccessLevel(), AccessLevel.PUBLIC);
		assertEquals(startMethod.getName(), "start");
		assertEquals(startMethod.getOwner(), carClass);
		assertEquals(startMethod.getReturnType(), "boolean");
	}
	
	@Test
	public void carKeyRelationTest() {
		Iterator<IRelation> relationIterator = model.getRelationIterator();
		boolean passed = false;
		while(relationIterator.hasNext()){
			IRelation relation = relationIterator.next();
			try{
				System.out.println(relation.getFirstClass().getName());
				System.out.println(relation.getSecondClass().getName());
				System.out.println(relation.getType());
				assertTrue(relation.getFirstClass().getName().equals("problem_asm_tests_classes_Car") &&
						relation.getSecondClass().getName().equals("problem_asm_tests_classes_Key") &&
						relation.getType() == RelationType.USES);
				passed = true;
			}catch( AssertionError e){
				
			}
		}
		if(!passed)
			fail("No associates relationship between car and key.");
	}

}
