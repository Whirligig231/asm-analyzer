package analyzer.visitor.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import analyzer.model.IMethod;
import analyzer.model.IMethodStatement;
import analyzer.model.IStatement;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class MethodSDOutputStream extends FilterOutputStream {
	private final IVisitor visitor;
	private Set<IMethod> visited; // This is to avoid endless loops otherwise caused by recursive functions
	
	public MethodSDOutputStream(OutputStream out) throws IOException {
		super(out);
		this.visited = new HashSet<IMethod>();
		this.visitor = new Visitor();
		this.setupPreVisitMethod();
		this.setupVisitStatement();
	}
	
	public void write(String m) {
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
	
	private void setupPreVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IMethod c = (IMethod)t;
				if (visited.contains(c))
					return;
				else
					visited.add(c);
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IMethod.class, command);
	}
	
	private void setupVisitStatement() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				if (!(t instanceof IMethodStatement))
					return;
				
				IMethodStatement c = (IMethodStatement)t;
				
				IMethod call = c.getMethod();
				
				//if (call.getName().equals("<init>")) {
				//	this.write(String.format("%s:%s.new\n", c.getOwner().getName(), call.getOwner().getName()));
				//}
				//else {
					String beginLine = String.format("%s:%s.%s(", c.getOwner().getName(), call.getOwner().getName(),
							call.getName());
					write(beginLine);
					for (int i = 0; i < call.getArgTypes().length; i++) {
						if (i > 0)
							write(", ");
						write(call.getArgTypes()[i]);
					}
					write(String.format(")\\: %s\n", call.getReturnType()));
				//}
	
				call.accept(visitor);
			}
		};
		this.visitor.addVisit(VisitType.Visit, IStatement.class, command);
	}
}
