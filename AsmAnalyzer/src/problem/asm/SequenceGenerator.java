package problem.asm;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequenceGenerator {
	
	private static SequenceGenerator instance;
	
	private Queue<MethodSignature> toVisit;
	
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
	}
	
	public void addMethod(String owner, String name, String desc, int level) {
		this.toVisit.add(new MethodSignature(owner, name, desc, level));
	}
	
	public void addMethodFromString(String signature) {
		this.addMethod(this.getMethodClass(signature),
				this.getMethodName(signature),
				this.getMethodDesc(signature),
				5);
	}
	
	public static SequenceGenerator getInstance() {
		if (instance == null)
			instance = new SequenceGenerator();
		
		return instance;
	}

	public static void main(String[] args) {
		String method = args[0];
		SequenceGenerator instance = getInstance();
		instance.addMethodFromString(method);
	}

}
