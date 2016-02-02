package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class ComponentClass extends AnnotatedClass {

	public ComponentClass(IClass decorated) {
		super(decorated, "Component", Color.green);
	}

}
