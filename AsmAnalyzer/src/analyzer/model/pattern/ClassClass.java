package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class ClassClass extends AnnotatedClass {

	public ClassClass(IClass decorated) {
		super(decorated, "Class", Color.cyan);
	}

}
