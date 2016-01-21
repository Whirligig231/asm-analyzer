package problem.asm;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import problem.asm.model.IClass;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.visitor.ITraverser;
import problem.asm.visitor.IVisitMethod;
import problem.asm.visitor.IVisitor;
import problem.asm.visitor.VisitType;
import problem.asm.visitor.Visitor;

public class MethodSDOutputStream extends FilterOutputStream {
	private final IVisitor visitor;
	private StringBuilder methodSection;
	
	public MethodSDOutputStream(OutputStream out) throws IOException {
		super(out);
		this.methodSection = new StringBuilder();
		this.visitor = new Visitor();
		this.setupPostVisitModel();
		this.setupPreVisitClass();
		this.setupVisitMethod();
	}
	
	private void write(String m) {
		try {
			super.write(m.getBytes());
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void write(IMethod m) {
		ITraverser t = (ITraverser)m;
		t.accept(this.visitor);
	}
	
	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				String line = String.format("%s:%s[a]\n", c.getName(), c.getName());
				write(line);
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}

	private void setupVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IMethod c = (IMethod)t;
				Iterator<IMethod> it = c.getCallIterator();
				while (it.hasNext()) {
					IMethod call = it.next();
					String line = String.format("%s:%s.%s\n", c.getOwner().getName(), call.getOwner().getName(),
							call.getName());
					methodSection.append(line);
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IMethod.class, command);
	}
	
	private void setupPostVisitModel() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IModel c = (IModel)t;
				write("\n");
				write(methodSection.toString());
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IModel.class, command);
	}
	
}
