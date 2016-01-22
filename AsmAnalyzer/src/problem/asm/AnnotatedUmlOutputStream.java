package problem.asm;

import java.io.IOException;
import java.io.OutputStream;

import problem.asm.model.IClass;
import problem.asm.pattern.IPattern;

public class AnnotatedUmlOutputStream extends ClassUmlOutputStream {
	
	ClassUmlOutputStream decorated;

	public AnnotatedUmlOutputStream(ClassUmlOutputStream out) throws IOException {
		super(out);
		this.decorated = out;
	}

	@Override
	protected String classNameHook(IClass c) {
		String out = this.decorated.classNameHook(c);
		System.out.println(c.getName());
		for (IPattern patt : c.getOwner().getPatterns(c)) {
			out += "\\n\\<\\<" + patt.getName() + "\\>\\>";
		}
		return out;
	}

	@Override
	protected String classFormatHook(IClass c) {
		String out = this.decorated.classFormatHook(c);
		String colorString = "";
		for (IPattern patt : c.getOwner().getPatterns(c)) {
			colorString = String.format(", color=\"#%02x%02x%02x\"", 
					patt.getColor().getRed(),
					patt.getColor().getGreen(),
					patt.getColor().getBlue());
		}
		return out + colorString;
	}

}
