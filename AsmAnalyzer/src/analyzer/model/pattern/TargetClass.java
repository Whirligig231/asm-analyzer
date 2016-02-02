package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class TargetClass extends AnnotatedClass {

	public TargetClass(IClass decorated) {
		super(decorated, "Target", Color.red);
	}

}
