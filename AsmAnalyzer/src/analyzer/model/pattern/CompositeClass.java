package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class CompositeClass extends AnnotatedClass {

	public CompositeClass(IClass decorated) {
		super(decorated, "Composite", new Color(255, 240, 0));
	}

}
