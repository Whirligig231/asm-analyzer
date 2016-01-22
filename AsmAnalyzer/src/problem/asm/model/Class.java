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
	private IModel model;
	
	private Map<String, IMethod> methods;
	private Map<String, IField> fields;
	
	public Class() {
		this.methods = new HashMap<>();
		this.fields = new HashMap<>();
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
		return this.fields.values().iterator();
	}

	@Override
	public void addField(IField field) {
		this.fields.put(field.getName() + ": " + field.getType(), field);
	}

	@Override
	public void accept(IVisitor v) {
		v.preVisit(this);
		for(IField field : this.fields.values()){
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

	@Override
	public IField getField(String name, String type) {
		return this.fields.get(name + ": " + type);
	}

	@Override
	public IModel getOwner() {
		return this.model;
	}

	@Override
	public void setOwner(IModel model) {
		this.model = model;
	}

}
