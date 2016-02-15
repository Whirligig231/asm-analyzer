package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class LeafClass extends AnnotatedClass {

	public LeafClass(IClass decorated) {
		super(decorated, "Leaf", new Color(255, 240, 0));
	}
}
