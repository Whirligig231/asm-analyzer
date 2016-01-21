package problem.asm;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import problem.asm.model.IMethod;
import problem.asm.visitor.ITraverser;
import problem.asm.visitor.IVisitMethod;
import problem.asm.visitor.IVisitor;
import problem.asm.visitor.VisitType;
import problem.asm.visitor.Visitor;

public class MethodSDOutputStream extends FilterOutputStream {
	private final IVisitor visitor;
	private StringBuilder methodSection;
	private Set<IMethod> visited; // This is to avoid endless loops otherwise caused by recursive functions
	
	public MethodSDOutputStream(OutputStream out) throws IOException {
		super(out);
		this.methodSection = new StringBuilder();
		this.visited = new HashSet<IMethod>();
		this.visitor = new Visitor();
		this.setupVisitMethod();
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
	
	private void setupVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IMethod c = (IMethod)t;
				if (visited.contains(c))
					return;
				else
					visited.add(c);
				
				Iterator<IMethod> it = c.getCallIterator();
				while (it.hasNext()) {
					IMethod call = it.next();
					
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
			}
		};
		this.visitor.addVisit(VisitType.Visit, IMethod.class, command);
	}
}
