package problem.asm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.RelationType;
import problem.asm.visitor.VisitorAdapter;

public class MethodSDOutputStream extends VisitorAdapter {

	private final OutputStream out;
	private Set<IMethod> visited; // This is to avoid endless loops otherwise caused by recursive functions
	
	public MethodSDOutputStream(OutputStream out) throws IOException {
		this.out = out;
		this.visited = new HashSet<IMethod>();
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
	public void visit(IMethod c) {
		// System.out.println("VISITING "+c.getOwner()+" METHOD "+c.getName()+c.getDesc());
		
		if (this.visited.contains(c))
			return;
		else
			this.visited.add(c);
		
		Iterator<IMethod> it = c.getCallIterator();
		while (it.hasNext()) {
			IMethod call = it.next();
			
			//if (call.getName().equals("<init>")) {
			//	this.write(String.format("%s:%s.new\n", c.getOwner().getName(), call.getOwner().getName()));
			//}
			//else {
				String beginLine = String.format("%s:%s.%s(", c.getOwner().getName(), call.getOwner().getName(),
						call.getName());
				this.write(beginLine);
				for (int i = 0; i < call.getArgTypes().length; i++) {
					if (i > 0)
						this.write(", ");
					this.write(call.getArgTypes()[i]);
				}
				this.write(String.format(")\\: %s\n", call.getReturnType()));
			//}

			call.accept(this);
		}
	}

}
