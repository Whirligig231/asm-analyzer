package problem.asm.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import problem.asm.ClassNameStandardizer;
import problem.asm.ClassUmlOutputStream;
import problem.asm.model.AccessLevel;
import problem.asm.model.Class;
import problem.asm.model.Field;
import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.Method;
import problem.asm.model.Model;
import problem.asm.model.Relation;
import problem.asm.model.RelationType;

public class ClassUmlOutputStreamTest {
	
	IModel singleClassModel;
	IModel extendsModel;
	IModel implementsModel;

	@Before
	public void setUp() throws Exception {
		singleClassModel = new Model();
		IClass carClass = new Class();
		carClass.setName(ClassNameStandardizer.standardize("Car"));
		singleClassModel.addClass(carClass);
		IMethod startMethod = new Method();
		startMethod.setName("start");
		startMethod.setAccessLevel(AccessLevel.PUBLIC);
		startMethod.setReturnType("void");
		String[] argTypes = {};
		startMethod.setArgTypes(argTypes);
		startMethod.setOwner(carClass);
		carClass.addMethod(startMethod);
		IField engineField = new Field();
		engineField.setAccessLevel(AccessLevel.PRIVATE);
		engineField.setName("engine");
		engineField.setType("Engine");
		carClass.addField(engineField);
		

		extendsModel = new Model();
		IClass vehicleClass = new Class();
		vehicleClass.setName(ClassNameStandardizer.standardize("Vehicle"));
		extendsModel.addClass(vehicleClass);
		extendsModel.addClass(carClass);
		IRelation relation = new Relation(carClass, vehicleClass, RelationType.EXTENDS);
		extendsModel.addRelation(relation);
		
		implementsModel = new Model();
		IClass drivableClass = new Class();
		drivableClass.setName(ClassNameStandardizer.standardize("Drivable"));
		implementsModel.addClass(vehicleClass);
		implementsModel.addClass(drivableClass);
		IRelation drivableVehicleRelation = new Relation(vehicleClass, drivableClass, RelationType.IMPLEMENTS);
		implementsModel.addRelation(drivableVehicleRelation);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSingleClassModel() {
		testUmlModelToFile(singleClassModel, "ExpectedOutputs/umltest1.txt");
	}
	
	@Test
	public void testExtendsModel() {
		testUmlModelToFile(extendsModel, "ExpectedOutputs/umltest2.txt");
	}
	
	@Test
	public void testImplementsModel() {
		testUmlModelToFile(implementsModel, "ExpectedOutputs/umltest3.txt");
	}
	
	
	
	private void testUmlModelToFile(IModel testModel, String filePath){
		OutputStream os = new ByteArrayOutputStream();
		try {
			ClassUmlOutputStream uml = new ClassUmlOutputStream(os);
			uml.write(testModel);
			BufferedReader br = new BufferedReader(new FileReader(filePath));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    
		    
			//assertEquals(os.toString(), sb.toString());
		    assertTrue(sb.toString().replaceAll("\r", "").replaceAll("\n",  "").contains(os.toString().replaceAll("\r",  "").replaceAll("\n",  "")));
		} catch (IOException e) {
			System.out.println("File not found: "+filePath);
			System.out.println("Printing actual output:");
			System.out.print(os.toString());
			fail("File not found");
		} catch (AssertionError e) {
			System.out.println("Test failed for file path: "+filePath);
			System.out.println("Printing actual output:");
			System.out.print(os.toString());
			fail("Test failed");
		}
	}

}
