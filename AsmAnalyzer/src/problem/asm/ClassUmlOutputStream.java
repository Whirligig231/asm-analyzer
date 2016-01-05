package problem.asm;

import java.io.IOException;
import java.io.OutputStream;

import problem.asm.model.IClass;
import problem.asm.model.IField;
import problem.asm.model.IMethod;
import problem.asm.visitor.VisitorAdapter;

public class ClassUmlOutputStream extends VisitorAdapter {

	private final OutputStream out;
	
	public ClassUmlOutputStream(OutputStream out) throws IOException {
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
		String line = String.format("%s [\n\tlabel = \"{%s|", c.getName(), c.getName());
		this.write(line);
	}
	@Override
	public void postFieldsVisit(IClass c) {
		this.write("|");
	}
	@Override
	public void postMethodsVisit(IClass c) {
		String line = String.format("}\"\n]\n");
		this.write(line);
	}
	@Override
	public void visit(IMethod c) {
		String line = String.format("%s %s() : %s\1", c.getAccessLevel(), c.getName(), c.getReturnType());
		this.write(line);
	}
	@Override
	public void visit(IField c) {
		String line = String.format("%s %s : %s\1", c.getAccessLevel(), c.getName(), c.getType());
		this.write(line);
	}
}
