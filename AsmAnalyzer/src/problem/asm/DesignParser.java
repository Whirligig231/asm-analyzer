package problem.asm;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import problem.asm.model.IClass;
import problem.asm.visitor.IVisitor;

public class DesignParser {
	/**
	 * Reads in a list of Java Classes and reverse engineers their design.
	 * 
	 * @param args: the names of the classes, separated by spaces. 
	 * 		For example: java DesignParser java.lang.String edu.rosehulman.csse374.ClassFieldVisitor java.lang.Math
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		System.out.println("digraph G {\nrankdir=BT;\n\nnode [\nfontname = \"Bitstream Vera Sans\"\nfontsize = 8\nshape = \"record\"\n]\nedge [\nfontname = \"Bitstream Vera Sans\"\nfontsize = 8\n]\n");
		
		for(String className: args){
			// ASM's ClassReader does the heavy lifting of parsing the compiled Java class
			ClassReader reader=new ClassReader(className);
			
			// make class declaration visitor to get superclass and interfaces
			ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5);
			
			// DECORATE declaration visitor with field visitor
			ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor);
			
			// DECORATE field visitor with method visitor
			ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor);
			
			IVisitor classUmlOutputStream = new ClassUmlOutputStream(System.out);

			// TODO: add more DECORATORS here in later milestones to accomplish specific tasks
			
			// Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
			reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
			
			IClass clazz = ((ClassMethodVisitor) methodVisitor).getClassModel();
			clazz.accept(classUmlOutputStream);

		}
		System.out.println("}");
	}
}
