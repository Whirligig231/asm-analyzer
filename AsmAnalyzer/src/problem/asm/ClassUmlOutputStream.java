package problem.asm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.RelationType;
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

	@Override
	public void visit(IRelation relation) {
		this.write(relation.getFirstClass().getName());
		this.write(" -> ");
		this.write(relation.getSecondClass().getName());
		switch (relation.getType()) {
		case EXTENDS:
			this.write(" [\n\tarrowhead = \"empty\"\n\tstyle = \"solid\"\n]\n\n");
			break;
		case IMPLEMENTS:
			this.write(" [\n\tarrowhead = \"empty\"\n\tstyle = \"dashed\"\n]\n\n");
			break;
		case ASSOCIATES:
			this.write(" [\n\tarrowhead = \"vee\"\n\tstyle = \"solid\"\n]\n\n");
			break;
		case USES:
			this.write(" [\n\tarrowhead = \"vee\"\n\tstyle = \"dashed\"\n]\n\n");
			break;
		}
	}

	@Override
	public void preVisit(IModel model) {
		this.write("digraph G {\nrankdir=BT;\n\nnode [\nfontname = \"Bitstream Vera Sans\"\nfontsize = 8\nshape = \"record\"\n]\nedge [\nfontname = \"Bitstream Vera Sans\"\nfontsize = 8\n]\n");

	}

	@Override
	public void postVisit(IModel model) {
		this.write("}");	
	}
}
