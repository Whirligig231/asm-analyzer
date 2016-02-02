package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class AdapterClass extends AnnotatedClass {

	public AdapterClass(IClass decorated) {
		super(decorated, "Adapter", Color.red);
	}

}
