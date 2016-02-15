package analyzer.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
import analyzer.visitor.detect.AdapterPatternDetector;
import analyzer.visitor.detect.CompositePatternDetector;
import analyzer.visitor.detect.DecoratorPatternComponentDetector;
import analyzer.visitor.detect.DecoratorPatternSubclassDetector;
import analyzer.visitor.detect.IPatternDetector;
import analyzer.visitor.detect.SingletonPatternDetector;
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
		
		Queue<IPatternDetector> detectors = new LinkedList<IPatternDetector>();
		detectors.offer(new SingletonPatternDetector());
		detectors.offer(new AdapterPatternDetector(params.get("adapter-threshold")));
		detectors.offer(new DecoratorPatternComponentDetector(params.get("decorator-threshold")));
		detectors.offer(new DecoratorPatternSubclassDetector());
		detectors.offer(new CompositePatternDetector());
		
		for(IPatternDetector detector : in(detectors.iterator())){
			detector.detect(model);
		}
		
		ClassUmlOutputStream classUmlOutputStream = new ClassUmlOutputStream(System.out);
		
		for (String className : args) {
			classUmlOutputStream.addClassName(ClassNameStandardizer.standardize(className));
		}
		classUmlOutputStream.write(model);
		classUmlOutputStream.close();
		
	}
	 public static <T> Iterable<T> in(final Iterator<T> iterator) {
		    assert iterator != null;
		    class SingleUseIterable implements Iterable<T> {
		      private boolean used = false;

		      @Override
		      public Iterator<T> iterator() {
		        if (used) {
		          throw new IllegalStateException("SingleUseIterable already invoked");
		        }
		        used = true;
		        return iterator;
		      }
		    }
		    return new SingleUseIterable();
		  }
}
