package analyzer.visitor.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IMethod;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.RelationType;
import analyzer.model.pattern.AnnotatedRelation;
import analyzer.model.pattern.IAnnotatedClass;
import analyzer.model.pattern.IAnnotatedRelation;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class ClassUmlOutputStream extends FilterOutputStream {
	private final IVisitor visitor;
	
	private Collection<String> classNames;
	
	public ClassUmlOutputStream(OutputStream out) throws IOException {
		super(out);
		this.visitor = new Visitor();
		this.setupPostFieldsVisitClass();
		this.setupPostMethodsVisitClass();
		this.setupPostVisitModel();
		this.setupPreVisitClass();
		this.setupPreVisitModel();
		this.setupVisitField();
		this.setupPreVisitMethod();
		this.setupVisitRelation();
		this.classNames = new ArrayList<>();
	}
	
	public void addClassName(String name) {
		this.classNames.add(name);
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
				
				if (!ClassUmlOutputStream.this.classNames.contains(c.getName()))
					return;
				
				String line = String.format("%s [\n\tlabel = \"{%s%s|", ClassNameStandardizer.standardize(c.getName()), ClassNameStandardizer.standardize(c.getName()).replaceAll("_", "."), ClassUmlOutputStream.this.getAnnotation(c));
				write(line);
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void setupPostFieldsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				if (!ClassUmlOutputStream.this.classNames.contains(c.getName()))
					return;
				write("|");
			}
		};
		this.visitor.addVisit(VisitType.Visit, IClass.class, command);
	}
	
	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				if (!ClassUmlOutputStream.this.classNames.contains(c.getName()))
					return;
				String line = String.format("}\"\n%s]\n", ClassUmlOutputStream.this.getFormatting(c));
				write(line);
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IClass.class, command);
	}

	private void setupPreVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IMethod c = (IMethod)t;
				if (!ClassUmlOutputStream.this.classNames.contains(c.getOwner().getName()))
					return;
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
		this.visitor.addVisit(VisitType.PreVisit, IMethod.class, command);
	}
	
	private void setupVisitField() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IField c = (IField)t;
				if (!ClassUmlOutputStream.this.classNames.contains(c.getOwner().getName()))
					return;
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
				if (!ClassUmlOutputStream.this.classNames.contains(firstClass))
					return;
				if (!ClassUmlOutputStream.this.classNames.contains(secondClass))
					return;
				if(!firstClass.equals(secondClass) || !relation.getType().equals(RelationType.USES)){
					write(firstClass + " -> " + secondClass);
					switch (relation.getType()) {
					case EXTENDS:
						write(" [\n\tarrowhead = \"empty\"\n\tstyle = \"solid\"");
						break;
					case IMPLEMENTS:
						write(" [\n\tarrowhead = \"empty\"\n\tstyle = \"dashed\"");
						break;
					case ASSOCIATES:
						write(" [\n\tarrowhead = \"vee\"\n\tstyle = \"solid\"");
						break;
					case USES:
						write(" [\n\tarrowhead = \"vee\"\n\tstyle = \"dashed\"");
						break;
					}
					
					if (relation instanceof IAnnotatedRelation) {
						IAnnotatedRelation ar = (IAnnotatedRelation)relation;
						write("\n\tlabel = \"\\<\\<" + ar.getAnnotation() + "\\>\\>\"");
					}
					
					write("\n]\n\n");
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
	
	private String getAnnotation(IClass c) {
		String out = "";
		if (c instanceof IAnnotatedClass) {
			IAnnotatedClass ac = (IAnnotatedClass)c;
			out += "\\n\\<\\<" + ac.getAnnotation() + "\\>\\>";
		}
		return out;
	}

	private String getFormatting(IClass c) {
		String out = "";
		if (c instanceof IAnnotatedClass) {
			IAnnotatedClass ac = (IAnnotatedClass)c;
			out += String.format(", color=\"#%02x%02x%02x\"", 
					ac.getColor().getRed(),
					ac.getColor().getGreen(),
					ac.getColor().getBlue());
		}
		return out;
	}
	
}
