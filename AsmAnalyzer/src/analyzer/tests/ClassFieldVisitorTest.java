package analyzer.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import analyzer.asmvisitor.visitclass.ClassDeclarationVisitor;
import analyzer.asmvisitor.visitclass.ClassFieldVisitor;
import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.Class;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.Model;
import analyzer.model.RelationType;

public class ClassFieldVisitorTest {
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
		
		ClassDeclarationVisitor cdvCar = new ClassDeclarationVisitor(Opcodes.ASM5, model);
		ClassFieldVisitor cfvCar = new ClassFieldVisitor(Opcodes.ASM5, cdvCar);
		ClassReader carReader =new ClassReader("problem.asm.tests.classes.Car");
		carReader.accept(cfvCar, ClassReader.EXPAND_FRAMES);
		carClass = cfvCar.getClassModel();
		
		ClassDeclarationVisitor cdvEngine = new ClassDeclarationVisitor(Opcodes.ASM5, model);
		ClassFieldVisitor cfvEngine = new ClassFieldVisitor(Opcodes.ASM5, cdvEngine);
		ClassReader engineReader = new ClassReader("problem.asm.tests.classes.Engine");
		engineReader.accept(cfvEngine, ClassReader.EXPAND_FRAMES);
		engineClass = cfvEngine.getClassModel();
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void carFieldsTest() {
		Iterator<IField> fieldIterator = carClass.getFieldIterator();
		IField engine = fieldIterator.next();
		assertEquals(engine.getAccessLevel(), AccessLevel.PUBLIC);
		assertEquals(engine.getName(), "engine");
		assertEquals(engine.getType(), "problem.asm.tests.classes.Engine");
		
	}
	
	@Test
	public void carEngineRelationTest() {
		Iterator<IRelation> relationIterator = model.getRelationIterator();
		boolean passed = false;
		while(relationIterator.hasNext()){
			IRelation relation = relationIterator.next();
			try{
//				System.out.println(relation.getFirstClass().getName());
//				System.out.println(relation.getSecondClass().getName());
//				System.out.println(relation.getType());
				assertTrue(relation.getFirstClass().getName().equals("problem_asm_tests_classes_Car") &&
						relation.getSecondClass().getName().equals("problem_asm_tests_classes_Engine") &&
						relation.getType() == RelationType.ASSOCIATES);
				passed = true;
			}catch( AssertionError e){
				
			}
		}
		if(!passed)
			fail("No associates relationship between car and engine.");
	}

}
