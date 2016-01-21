package problem.asm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import problem.asm.visitor.IVisitor;

public class Class implements IClass {
	
	private String name;
	private AccessLevel accessLevel;
	
	private Map<String, IMethod> methods;
	private Collection<IField> fields;
	
	public Class() {
		this.methods = new HashMap<>();
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
		return this.methods.values().iterator();
	}

	@Override
	public void addMethod(IMethod method) {
		this.methods.put(method.getName() + method.getDesc(), method);
		// System.out.println(this.toString()+" add method "+method.getName() + method.getDesc());
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
		v.visit(this);
		for(IMethod method : this.methods.values()){
			method.accept(v);
		}
		v.postVisit(this);
	}

	@Override
	public IMethod getMethod(String name, String desc) {
		// if (this.methods.get(name + desc) == null)
			// System.out.println(this.toString()+" has no method "+name+desc);
		return this.methods.get(name + desc);
	}

}
