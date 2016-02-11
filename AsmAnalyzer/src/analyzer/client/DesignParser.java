package analyzer.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import analyzer.visitor.detect.AdapterDetector;
import analyzer.visitor.detect.CompositeDetector;
import analyzer.visitor.detect.DecoratorDetector;
import analyzer.visitor.detect.DecoratorSubclassDetector;
import analyzer.visitor.detect.SingletonDetector;
import analyzer.visitor.output.ClassUmlOutputStream;

public class DesignParser {
	
	/**
	 * Reads in a list of Java Classes and reverse engineers their design.
	 * 
	 * @param args: the names of the classes, separated by spaces. 
	 * 		For example: java DesignParser java.lang.String edu.rosehulman.csse374.ClassFieldVisitor java.lang.Math
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		
		IModel model = new Model();
		
		Map<String, Integer> params = new HashMap<>();
		params.put("decorator-threshold", 3);
		params.put("adapter-threshold", 3);
				
		for (String argument: args) {
			
			if (argument.contains("=")) {
				String pname = argument.substring(0, argument.indexOf('='));
				String pvalue = argument.substring(argument.indexOf('=') + 1);
				try {
					int pnumber = Integer.parseInt(pvalue);
					params.put(pname, pnumber);
				}
				catch (NumberFormatException ex) {
					System.out.println("// Warning: non-numeric parameter value \"" +
							pvalue + "\" for parameter \"" + pname + "\" was ignored\n");
				}
			}
			else {
				//System.out.println("Loading class: "+className);
				IClass classModel = new Class();
				classModel.setName(ClassNameStandardizer.standardize(argument));
				classModel.setOwner(model);
				model.addClass(classModel);
			}

		}
		
		for (String argument: args) {
			
			if (argument.contains("-"))
				continue;
			
			//System.out.println("Loading class: " + className);
			// ASM's ClassReader does the heavy lifting of parsing the compiled Java class
			ClassReader reader=new ClassReader(argument);
			
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
		
		AdapterDetector ad = new AdapterDetector(params.get("adapter-threshold"));
		ad.detect(model);
		
		DecoratorDetector dd = new DecoratorDetector(params.get("decorator-threshold"));
		dd.detect(model);
		
		// Propagates detection down to subclasses
		DecoratorSubclassDetector dsd = new DecoratorSubclassDetector();
		dsd.detect(model);

		CompositeDetector cd = new CompositeDetector();
		cd.detect(model);
		
		ClassUmlOutputStream classUmlOutputStream = new ClassUmlOutputStream(System.out);
		
		for (String className : args) {
			classUmlOutputStream.addClassName(ClassNameStandardizer.standardize(className));
		}
		classUmlOutputStream.write(model);
		classUmlOutputStream.close();
		
	}
}
