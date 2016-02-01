package analyzer.model.pattern;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import analyzer.model.IClass;
import analyzer.visitor.common.IVisitor;

public abstract class Pattern implements IPattern {
	ArrayList<IClass> classes;
	
	public Pattern(){
		classes = new ArrayList<IClass>();
	}

	@Override
	public Collection<IClass> getClasses() {
		return classes;
	}

	@Override
	public abstract Color getColor();

	@Override
	public abstract String getName();

	@Override
	public void addClass(IClass clazz) {
		classes.add(clazz);
	}

	@Override
	public void removeClass(IClass clazz) {
		classes.remove(clazz);
	}

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

}
