package problem.asm;

import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

public class FirstASM {
	public static String myField = "Hello World!";
	
	public static void main(String[] args) throws IOException{
		// Read in this class
		ClassReader reader=new ClassReader("problem.asm.FirstASM");

		// Construct visitor to print out byte code
		ClassVisitor visitor = new TraceClassVisitor(new PrintWriter(System.out));
		
		// Tell Reader to visit the code with this Visitor
		reader.accept(visitor, ClassReader.EXPAND_FRAMES);
	}
}
