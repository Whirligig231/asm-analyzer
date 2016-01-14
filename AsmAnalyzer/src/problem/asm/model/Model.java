package problem.asm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import problem.asm.visitor.IVisitor;

public class Model implements IModel {
	
	private Map<String, IClass> classes;
	private Collection<IRelation> relations;

	public Model() {
		this.classes = new HashMap<>();
		this.relations = new HashSet<>();
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
					RelationType.ASSOCIATES);
			if (this.relations.contains(assoc))
				return;
		}
		else if (relation.getType() == RelationType.ASSOCIATES) {
			IRelation use = new Relation(relation.getFirstClass(), relation.getSecondClass(),
					RelationType.USES);
			if (this.relations.contains(use))
				this.relations.remove(use);
		}
		
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

}
