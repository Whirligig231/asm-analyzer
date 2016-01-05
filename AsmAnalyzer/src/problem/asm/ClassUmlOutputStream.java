package problem.asm;

import java.io.IOException;
import java.io.OutputStream;

import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.visitor.VisitorAdapter;

public class ClassUmlOutputStream extends VisitorAdapter {

	private final OutputStream out;
	
	public ClassUmlOutputStream(OutputStream out) throws IOException {
		this.out = out;
	}
	
	private void write(String m) {
		try {
			this.out.write(m.getBytes());
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void preVisit(IClass c) {
		String line = String.format("%s [\n\tlabel = \"{%s|", c.getName(), c.getName());
		this.write(line);
	}
	@Override
	public void postFieldsVisit(IClass c) {
		this.write("|");
	}
	@Override
	public void postMethodsVisit(IClass c) {
		String line = String.format("}\"\n]\n");
		this.write(line);
		
		String[] interfaces = c.getInterfaces();
		String superClass = c.getSuperClass();
		
		if(interfaces.length != 0){
			
			this.write("edge [\n\tarrowhead = \"empty\"\n\tstyle = \"dashed\"\n]\n");
			for(String inter : interfaces){
				line = String.format("%s -> %s\n", c.getName(), inter);
				this.write(line);
			}
		}
		
		if(superClass != null && !superClass.equals("")){
			this.write("edge [\n\tarrowhead = \"empty\"\n\tstyle = \"solid\"\n]\n");
			line = String.format("%s -> %s\n", c.getName(), superClass);
			this.write(line);
		}
	}
	@Override
	public void visit(IMethod c) {
		StringBuilder sb = new StringBuilder();
		String line = String.format("%s %s(", c.getAccessLevel(), c.getName());
		sb.append(line);
		boolean ran = false;
		for(String argType : c.getArgTypes()){
			ran=true;
			sb.append(argType);
			sb.append(", ");
		}
		if(ran)
			sb.setLength(sb.length()-2); //getting rid of the last unneeded ", "
		
		line = String.format(") : %s\1", c.getReturnType());
		
		sb.append(line);
		
		this.write(sb.toString());
	}
	
	@Override
	public void visit(IField c) {
		String line = String.format("%s %s : %s\1", c.getAccessLevel(), c.getName(), c.getType());
		this.write(line);
	}
}
