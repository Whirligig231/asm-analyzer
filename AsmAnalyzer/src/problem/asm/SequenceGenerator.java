package problem.asm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import problem.asm.model.IClass;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.model.Model;
import problem.asm.visitor.IVisitor;

public class SequenceGenerator {
	
	private static SequenceGenerator instance;
	
	private Queue<MethodSignature> toVisit;
	private IModel model;
	
	private String getASMClass(String dottedClass) {
		return dottedClass.replaceAll("\\.","/");
	}
	
	private String getASMType(String type) {
		String asmType = "";
		while (type.endsWith("[]")) {
			asmType += "[";
			type = type.substring(0, type.length()-2);
		}
		if (type.equals("void"))
			return asmType + "V";
		if (type.equals("boolean"))
			return asmType + "Z";
		if (type.equals("char"))
			return asmType + "C";
		if (type.equals("byte"))
			return asmType + "B";
		if (type.equals("short"))
			return asmType + "S";
		if (type.equals("int"))
			return asmType + "I";
		if (type.equals("float"))
			return asmType + "F";
		if (type.equals("long"))
			return asmType + "J";
		if (type.equals("double"))
			return asmType + "D";
		
		return asmType + "L" + getASMClass(type) + ";";
	}
	
	private String getASMDesc(String signature) {
		signature = signature.substring(1, signature.length() - 1);
		String[] types = signature.split(",");
		
		String asmDesc = "(";
		for (String type : types) {
			asmDesc += getASMType(type.trim());
		}
		
		return asmDesc + ")";
	}
	
	private String getMethodClass(String method) {
		Matcher m = Pattern.compile("([a-zA-Z0-9_.]+)\\.([a-zA-Z0-9_]+)\\(.*").matcher(method);
		if (m.matches()) {
			return m.group(1);
		}
		return "";
	}
	
	private String getMethodName(String method) {
		Matcher m = Pattern.compile("([a-zA-Z0-9_.]+)\\.([a-zA-Z0-9_]+)\\(.*").matcher(method);
		if (m.matches()) {
			return m.group(2);
		}
		return "";
	}
	
	private String getMethodDesc(String method) {
		Matcher m = Pattern.compile("([a-zA-Z0-9_.]+)\\.([a-zA-Z0-9_]+)(\\(.*\\))").matcher(method);
		if (m.matches()) {
			return getASMDesc(m.group(3));
		}
		return "";
	}
	
	private SequenceGenerator() {
		this.toVisit = new LinkedList<>();
		this.model = new Model();
	}
	
	public void addMethod(String owner, String name, String desc, int level) {
		this.toVisit.add(new MethodSignature(owner, name, desc, level));
	}
	
	public void addMethodFromString(String signature, int depth) {
		this.addMethod(this.getMethodClass(signature),
				this.getMethodName(signature),
				this.getMethodDesc(signature),
				depth); // TODO: make this not hard-coded but use the input parameter
	}
	
	public MethodSignature getNextMethod() {
		return this.toVisit.peek();
	}
	
	public MethodSignature popNextMethod() {
		return this.toVisit.remove();
	}
	
	public void writeSequence(String method, OutputStream out) throws IOException {
		
		ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, model);
		ClassMethodVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, decVisitor);
		ClassVisitor callsVisitor = new ClassCallsVisitor(Opcodes.ASM5, methodVisitor, this);
		
		while (!this.toVisit.isEmpty()) {

			ClassReader cr = new ClassReader(ClassNameStandardizer.forASM(this.toVisit.peek().getOwner()));
			cr.accept(callsVisitor, ClassReader.EXPAND_FRAMES);
			
		}
		
		ClassSDOutputStream classSDOutputStream = new ClassSDOutputStream(System.out);
		classSDOutputStream.write(model);
		
		MethodSDOutputStream methodSDOutputStream = new MethodSDOutputStream(System.out);
		
		// Find the appropriate method to visit first
		String className = ClassNameStandardizer.standardize(this.getMethodClass(method));
		IClass classModel = model.getClass(className);
		
		String methodName = this.getMethodName(method);
		String methodDesc = this.getMethodDesc(method);
		Iterator<IMethod> it = classModel.getMethodIterator();
		IMethod methodModel = null;
		while (it.hasNext()) {
			IMethod thisMethod = it.next();
			if (thisMethod.getName().equals(methodName) && thisMethod.getDesc().startsWith(methodDesc)) 
				methodModel = thisMethod;
		}
		
		methodSDOutputStream.write(methodModel);
	}
	
	public static SequenceGenerator getInstance() {
		if (instance == null)
			instance = new SequenceGenerator();
		
		return instance;
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: SequenceGenerator <methodSignature> [depth=5]");
		}
		String method = args[0];
		
		int depth = 5;
		if (args.length > 1) {
			try {
				depth = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException ex) {
				depth = 5;
			}
		}
		
		SequenceGenerator instance = getInstance();
		instance.addMethodFromString(method, depth);
		instance.writeSequence(method, System.out);
	}

}
