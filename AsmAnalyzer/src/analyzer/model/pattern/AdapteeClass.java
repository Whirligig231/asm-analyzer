package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class AdapteeClass extends AnnotatedClass {

	public AdapteeClass(IClass decorated) {
		super(decorated, "Adaptee", Color.red);
	}

}
