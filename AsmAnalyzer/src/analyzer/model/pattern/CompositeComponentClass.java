package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class CompositeComponentClass extends AnnotatedClass {

	public CompositeComponentClass(IClass decorated) {
		super(decorated, "Component", Color.yellow);
	}
}
