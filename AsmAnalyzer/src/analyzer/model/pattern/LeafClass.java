package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class LeafClass extends AnnotatedClass {

	public LeafClass(IClass decorated) {
		super(decorated, "Leaf", Color.yellow);
	}
}
