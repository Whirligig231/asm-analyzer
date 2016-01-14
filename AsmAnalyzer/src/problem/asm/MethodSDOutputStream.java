package problem.asm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.model.IModel;
import problem.asm.model.IRelation;
import problem.asm.model.RelationType;
import problem.asm.visitor.VisitorAdapter;

public class MethodSDOutputStream extends VisitorAdapter {

	private final OutputStream out;
	private StringBuilder methodSection;
	
	public MethodSDOutputStream(OutputStream out) throws IOException {
		this.out = out;
		this.methodSection = new StringBuilder();
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
		// System.out.println("CLASS IS "+c+" "+c.getName());
		String line = String.format("%s:%s[a]\n", c.getName(), c.getName());
		this.write(line);
	}

	@Override
	public void visit(IMethod c) {
		// System.out.println("VISITING "+c.getOwner()+" METHOD "+c.getName()+c.getDesc());
		Iterator<IMethod> it = c.getCallIterator();
		while (it.hasNext()) {
			IMethod call = it.next();
			String line = String.format("%s:%s.%s\n", c.getOwner().getName(), call.getOwner().getName(),
					call.getName());
			this.methodSection.append(line);
		}
	}
	
	@Override
	public void postVisit(IModel m) {
		this.write("\n");
		this.write(this.methodSection.toString());
	}
	
}
