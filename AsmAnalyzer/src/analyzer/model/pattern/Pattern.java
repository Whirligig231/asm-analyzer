package analyzer.model.pattern;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import analyzer.model.IClass;
import analyzer.visitor.common.IVisitor;

public class Pattern implements IPattern {
	
	private Collection<IClass> classes;
	private final String name;
	private final Color color;
	
	public Pattern(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	@Override
	public Iterator<IClass> iterator() {
		return this.classes.iterator();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void addClass(IClass classModel) {
		this.classes.add(classModel);
	}

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

}
