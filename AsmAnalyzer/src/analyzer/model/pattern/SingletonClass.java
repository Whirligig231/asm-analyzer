package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public class SingletonClass extends AnnotatedClass {

	public SingletonClass(IClass decorated) {
		super(decorated, "Singleton", Color.blue);
	}

}
