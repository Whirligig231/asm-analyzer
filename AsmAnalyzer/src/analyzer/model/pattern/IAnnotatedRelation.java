package analyzer.model.pattern;

import java.awt.Color;

import analyzer.model.IRelation;

public interface IAnnotatedRelation extends IRelation {

	public String getAnnotation();
	public Color getColor();

}
