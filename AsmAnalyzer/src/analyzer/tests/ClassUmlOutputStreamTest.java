package analyzer.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.Class;
import analyzer.model.Field;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IMethod;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.Method;
import analyzer.model.Model;
import analyzer.model.Relation;
import analyzer.model.RelationType;
import analyzer.visitor.output.ClassUmlOutputStream;

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
		IMethod startMethod = new Method(singleClassModel);
		startMethod.setName("start");
		startMethod.setAccessLevel(AccessLevel.PUBLIC);
		startMethod.setReturnType("void");
		String[] argTypes = {};
		startMethod.setArgTypes(argTypes);
		startMethod.setOwner(carClass);
		carClass.addMethod(startMethod);
		IField engineField = new Field(singleClassModel);
		engineField.setAccessLevel(AccessLevel.PRIVATE);
		engineField.setName("engine");
		engineField.setType("Engine");
		carClass.addField(engineField);
		

		extendsModel = new Model();
		IClass vehicleClass = new Class();
		vehicleClass.setName(ClassNameStandardizer.standardize("Vehicle"));
		extendsModel.addClass(vehicleClass);
		extendsModel.addClass(carClass);
		IRelation relation = new Relation(carClass, vehicleClass, RelationType.EXTENDS, extendsModel);
		extendsModel.addRelation(relation);
		
		implementsModel = new Model();
		IClass drivableClass = new Class();
		drivableClass.setName(ClassNameStandardizer.standardize("Drivable"));
		implementsModel.addClass(vehicleClass);
		implementsModel.addClass(drivableClass);
		IRelation drivableVehicleRelation = new Relation(vehicleClass, drivableClass, RelationType.IMPLEMENTS, implementsModel);
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
