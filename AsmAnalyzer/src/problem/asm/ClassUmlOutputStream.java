package problem.asm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

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
		String line = String.format("%s [\n\tlabel = \"{%s|", c.getName().replaceAll("\\/", "_"), c.getName().replaceAll("\\/", "."));
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
		Collection<String> associates = c.getAssociates();
		Collection<String> uses = c.getUses();
		
		if(interfaces.length != 0){
			
			this.write("edge [\n\tarrowhead = \"empty\"\n\tstyle = \"dashed\"\n]\n");
			for(String inter : interfaces){
				line = String.format("%s -> %s\n", c.getName().replaceAll("\\/", "_"), inter.replaceAll("\\/", "_"));
				this.write(line);
			}
		}
		
		if(superClass != null && !superClass.equals("") && !superClass.equals("java/lang/Object")){
			this.write("edge [\n\tarrowhead = \"empty\"\n\tstyle = \"solid\"\n]\n");
			line = String.format("%s -> %s\n", c.getName().replaceAll("\\/", "_"), superClass.replaceAll("\\/", "_"));
			this.write(line);
		}
		
		if(associates.size() != 0){
			
			this.write("edge [\n\tarrowhead = \"vee\"\n\tstyle = \"solid\"\n]\n");
			for(String assoc : associates){
				line = String.format("%s -> %s\n", c.getName().replaceAll("\\/", "_"), assoc.replaceAll("[./$]", "_"));
				this.write(line);
			}
		}
		
		if(uses.size() != 0){
			
			this.write("edge [\n\tarrowhead = \"vee\"\n\tstyle = \"dashed\"\n]\n");
			for(String use : uses){
				line = String.format("%s -> %s\n", c.getName().replaceAll("\\/", "_"), use.replaceAll("[./$]", "_"));
				this.write(line);
			}
		}
	}
	@Override
	public void visit(IMethod c) {
		StringBuilder sb = new StringBuilder();
		String line = String.format("%s %s(", c.getAccessLevel(), c.getName().replaceAll("<", "\\\\<").replaceAll(">", "\\\\>"));
		sb.append(line);
		boolean ran = false;
		for(String argType : c.getArgTypes()){
			ran=true;
			sb.append(argType);
			sb.append(", ");
		}
		if(ran)
			sb.setLength(sb.length()-2); //getting rid of the last unneeded ", "
		
		line = String.format(") : %s\\l", c.getReturnType());
		
		sb.append(line);
		
		this.write(sb.toString());
	}
	
	@Override
	public void visit(IField c) {
		String line = String.format("%s %s : %s\\l", c.getAccessLevel(), c.getName(), c.getType());
		this.write(line);
	}
}
