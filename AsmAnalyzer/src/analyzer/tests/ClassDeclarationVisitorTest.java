package analyzer.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import analyzer.asmvisitor.visitclass.ClassDeclarationVisitor;
import analyzer.model.AccessLevel;
import analyzer.model.IClass;
import analyzer.model.IModel;
import analyzer.model.Model;

public class ClassDeclarationVisitorTest {
	ClassDeclarationVisitor cdv;
	IModel model;
	IClass carClass;

	@Before
	public void setUp() throws Exception {
		model = new Model();
		
		cdv = new ClassDeclarationVisitor(Opcodes.ASM5, model);
		ClassReader reader=new ClassReader("problem.asm.tests.classes.Car");
		reader.accept(cdv, ClassReader.EXPAND_FRAMES);
		carClass = cdv.getClassModel();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals(carClass.getAccessLevel(), AccessLevel.PUBLIC);
		assertEquals(carClass.getName(), "problem_asm_tests_classes_Car");
		assertTrue(model.getClass("problem_asm_tests_classes_Car") != null);
	}

}
