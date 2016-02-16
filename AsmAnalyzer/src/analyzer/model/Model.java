package analyzer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import analyzer.model.pattern.IPattern;
import analyzer.visitor.common.IVisitor;

public class Model implements IModel {
	
	private Map<String, IClass> classes;
	private Collection<IRelation> relations;
	private Collection<IPattern> patterns;

	public Model() {
		this.classes = new HashMap<>();
		this.relations = new HashSet<>();
		this.patterns = new ArrayList<>();
	}

	@Override
	public void accept(IVisitor v) {
		v.preVisit(this);
		for(IClass classModel : this.classes.values()){
			classModel.accept(v);
		}
		for(IRelation relation : this.relations){
			relation.accept(v);
		}
		for(IPattern pattern : this.patterns){
			pattern.accept(v);
		}
		v.postVisit(this);
	}

	@Override
	public Iterator<IClass> getClassIterator() {
		return this.classes.values().iterator();
	}

	@Override
	public Iterator<IRelation> getRelationIterator() {
		return this.relations.iterator();
	}

	@Override
	public void addRelation(IRelation relation) {
		// Association overrides use
		if (relation.getType() == RelationType.USES) {
			IRelation assoc = new Relation(relation.getFirstClass(), relation.getSecondClass(),
					RelationType.ASSOCIATES, this);
			if (this.relations.contains(assoc))
				return;
		}
		else if (relation.getType() == RelationType.ASSOCIATES) {
			IRelation use = new Relation(relation.getFirstClass(), relation.getSecondClass(),
					RelationType.USES, this);
			if (this.relations.contains(use))
				this.relations.remove(use);
		}
		
		if (this.relations.contains(relation))
			this.relations.remove(relation);
		this.relations.add(relation);
	}

	@Override
	public void addClass(IClass classModel) {
		this.classes.put(classModel.getName(), classModel);
	}

	@Override
	public IClass getClass(String className) {
		return this.classes.get(className);
	}

	@Override
	public Iterator<IPattern> getPatternIterator() {
		return this.patterns.iterator();
	}

	@Override
	public void addPattern(IPattern pattern) {
		this.patterns.add(pattern);
	}

}
