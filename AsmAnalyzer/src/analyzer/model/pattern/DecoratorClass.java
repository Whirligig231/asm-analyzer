package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class DecoratorClass extends AnnotatedClass {

	public DecoratorClass(IClass decorated) {
		super(decorated, "Decorator", Color.green);
	}

}
