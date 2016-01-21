package problem.asm;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import problem.asm.model.IClass;
import problem.asm.model.IModel;
import problem.asm.visitor.ITraverser;
import problem.asm.visitor.IVisitMethod;
import problem.asm.visitor.IVisitor;
import problem.asm.visitor.VisitType;
import problem.asm.visitor.Visitor;

public class ClassSDOutputStream extends FilterOutputStream {
	private final IVisitor visitor;
	
	public ClassSDOutputStream(OutputStream out) throws IOException {
		super(out);
		this.visitor = new Visitor();
		this.setupPostVisitModel();
		this.setupPreVisitClass();
	}
	
	private void write(String m) {
		try {
			this.out.write(m.getBytes());
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void write(IModel m) {
		ITraverser t = (ITraverser) m;
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

	private void setupPostVisitModel() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IModel c = (IModel)t;
				write("\n");
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IModel.class, command);
	}
	
}
