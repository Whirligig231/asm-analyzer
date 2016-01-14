package problem.asm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import problem.asm.visitor.IVisitor;

public class Class implements IClass {
	
	private String name;
	private AccessLevel accessLevel;
	
	private Collection<IMethod> methods;
	private Collection<IField> fields;
	
	public Class() {
		this.methods = new ArrayList<>();
		this.fields = new ArrayList<>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AccessLevel getAccessLevel() {
		return this.accessLevel;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	@Override
	public Iterator<IMethod> getMethodIterator() {
		return this.methods.iterator();
	}

	@Override
	public void addMethod(IMethod method) {
		this.methods.add(method);
	}

	@Override
	public Iterator<IField> getFieldIterator() {
		return this.fields.iterator();
	}

	@Override
	public void addField(IField field) {
		this.fields.add(field);
	}

	@Override
	public void accept(IVisitor v) {
		v.preVisit(this);
		for(IField field : this.fields){
			field.accept(v);
		}
		v.postFieldsVisit(this);
		for(IMethod method : this.methods){
			method.accept(v);
		}
		v.postMethodsVisit(this);
	}

}
