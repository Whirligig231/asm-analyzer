package analyzer.visitor.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import analyzer.model.IClass;
import analyzer.model.IModel;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

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
