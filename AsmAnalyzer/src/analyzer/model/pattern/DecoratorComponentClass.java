package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class DecoratorComponentClass extends AnnotatedClass {

	public DecoratorComponentClass(IClass decorated) {
		super(decorated, "Component", Color.green);
	}

}
