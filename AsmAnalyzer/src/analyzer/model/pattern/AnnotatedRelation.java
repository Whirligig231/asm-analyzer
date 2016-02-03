package analyzer.model.pattern;

import analyzer.model.IClass;
import analyzer.model.IRelation;
import analyzer.model.Relation;
import analyzer.model.RelationType;
import analyzer.visitor.common.IVisitor;

public class AnnotatedRelation implements IAnnotatedRelation {
	
	private IRelation decorated;
	private String annotation;

	public AnnotatedRelation(IRelation decorated, String annotation) {
		this.decorated = decorated;
		this.annotation = annotation;
	}

	@Override
	public IClass getFirstClass() {
		return this.decorated.getFirstClass();
	}

	@Override
	public IClass getSecondClass() {
		return this.decorated.getSecondClass();
	}

	@Override
	public RelationType getType() {
		return this.decorated.getType();
	}

	@Override
	public void setFirstClass(IClass firstClass) {
		this.decorated.setFirstClass(firstClass);
	}

	@Override
	public void setSecondClass(IClass secondClass) {
		this.decorated.setSecondClass(secondClass);
	}

	@Override
	public void setType(RelationType type) {
		this.decorated.setType(type);
	}

	@Override
	public void accept(IVisitor v) {
		v.visit(this);
	}

	@Override
	public String getAnnotation() {
		return this.annotation;
	}

	@Override
	public boolean equals(Object obj) {
		return this.decorated.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return this.decorated.hashCode();
	}
	
}
