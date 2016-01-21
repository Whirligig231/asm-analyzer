package problem.asm;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.visitor.ITraverser;
import problem.asm.visitor.IVisitMethod;
import problem.asm.visitor.IVisitor;
import problem.asm.visitor.VisitType;
import problem.asm.visitor.Visitor;

public class ClassUmlOutputStream extends FilterOutputStream {
	private final IVisitor visitor;
	
	public ClassUmlOutputStream(OutputStream out) throws IOException {
		super(out);
		this.visitor = new Visitor();
		this.setupPostFieldsVisitClass();
		this.setupPostMethodsVisitClass();
		this.setupPostVisitModel();
		this.setupPreVisitClass();
		this.setupPreVisitModel();
		this.setupVisitField();
		this.setupVisitMethod();
		this.setupVisitRelation();
	}
	
	private void write(String m) {
		try {
			super.write(m.getBytes());
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void write (IModel model) {
		ITraverser t = (ITraverser) model;
		t.accept(this.visitor);
	}
	
	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				String line = String.format("%s [\n\tlabel = \"{%s|", c.getName().replaceAll("\\/", "_"), c.getName().replaceAll("\\/", "."));
				write(line);
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void setupPostFieldsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				write("|");
			}
		};
		this.visitor.addVisit(VisitType.Visit, IClass.class, command);
	}
	
	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				String line = String.format("}\"\n]\n");
				write(line);
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IClass.class, command);
	}
	
	private void setupVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IMethod c = (IMethod)t;
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
				
				write(sb.toString());
			}
		};
		this.visitor.addVisit(VisitType.Visit, IMethod.class, command);
	}
	
	private void setupVisitField() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IField c = (IField)t;
				String line = String.format("%s %s : %s\\l", c.getAccessLevel(), c.getName(), c.getType());
				write(line);
			}
		};
		this.visitor.addVisit(VisitType.Visit, IField.class, command);
	}

	private void setupVisitRelation() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IRelation relation = (IRelation)t;
				String firstClass = relation.getFirstClass().getName();
				String secondClass = relation.getSecondClass().getName();
				if(!firstClass.equals(secondClass)){
					write(firstClass + " -> " + secondClass);
					switch (relation.getType()) {
					case EXTENDS:
						write(" [\n\tarrowhead = \"empty\"\n\tstyle = \"solid\"\n]\n\n");
						break;
					case IMPLEMENTS:
						write(" [\n\tarrowhead = \"empty\"\n\tstyle = \"dashed\"\n]\n\n");
						break;
					case ASSOCIATES:
						write(" [\n\tarrowhead = \"vee\"\n\tstyle = \"solid\"\n]\n\n");
						break;
					case USES:
						write(" [\n\tarrowhead = \"vee\"\n\tstyle = \"dashed\"\n]\n\n");
						break;
					}
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IRelation.class, command);
	}

	private void setupPreVisitModel() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				write("digraph G {\nrankdir=BT;\n\nnode [\nfontname = \"Bitstream Vera Sans\"\nfontsize = 8\nshape = \"record\"\n]\nedge [\nfontname = \"Bitstream Vera Sans\"\nfontsize = 8\n]\n");
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IModel.class, command);
	}

	private void setupPostVisitModel() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				write("}\n");
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IModel.class, command);
	}
}
