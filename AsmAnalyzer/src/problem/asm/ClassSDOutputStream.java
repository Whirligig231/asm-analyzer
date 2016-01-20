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

public class ClassSDOutputStream extends VisitorAdapter {

	private final OutputStream out;
	
	public ClassSDOutputStream(OutputStream out) throws IOException {
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
		// System.out.println("CLASS IS "+c+" "+c.getName());
		String line = String.format("%s:%s[a]\n", c.getName(), c.getName());
		this.write(line);
	}

	@Override
	public void postVisit(IModel m) {
		this.write("\n");
	}
	
}
