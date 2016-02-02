package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IClass;

public interface IAnnotatedClass extends IClass {
	
	public String getAnnotation();
	public Color getColor();

}
