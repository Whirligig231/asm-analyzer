package problem.asm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

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
	
	public void addMethodFromString(String signature) {
		this.addMethod(this.getMethodClass(signature),
				this.getMethodName(signature),
				this.getMethodDesc(signature),
				2); // TODO: make this not hard-coded but use the input parameter
	}
	
	public MethodSignature getNextMethod() {
		return this.toVisit.peek();
	}
	
	public MethodSignature popNextMethod() {
		return this.toVisit.remove();
	}
	
	public void writeSequence(OutputStream out) throws IOException {
		
		ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, model);
		ClassMethodVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, decVisitor);
		ClassVisitor callsVisitor = new ClassCallsVisitor(Opcodes.ASM5, methodVisitor, this);
		
		while (!this.toVisit.isEmpty()) {
			
			ClassReader cr = new ClassReader(this.toVisit.peek().getOwner());
			cr.accept(callsVisitor, ClassReader.EXPAND_FRAMES);
			
		}
		
		IVisitor methodSDOutputStream = new MethodSDOutputStream(System.out);
		model.accept(methodSDOutputStream);
		
	}
	
	public static SequenceGenerator getInstance() {
		if (instance == null)
			instance = new SequenceGenerator();
		
		return instance;
	}

	public static void main(String[] args) throws IOException {
		String method = args[0];
		SequenceGenerator instance = getInstance();
		instance.addMethodFromString(method);
		instance.writeSequence(System.out);
	}

}
