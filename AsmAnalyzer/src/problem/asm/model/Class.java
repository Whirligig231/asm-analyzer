package problem.asm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import problem.asm.visitor.IVisitor;

public class Class implements IClass {
	
	private String name;
	private AccessLevel accessLevel;
	private String superClass;
	private String[] interfaces;
	private Collection<String> associates;
	private Collection<String> uses;
	
	private Collection<IMethod> methods;
	private Collection<IField> fields;
	
	public Class() {
		this.methods = new ArrayList<>();
		this.fields = new ArrayList<>();
		this.associates = new ArrayList<>();
		this.uses = new ArrayList<>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AccessLevel getAccessLevel() {
		return this.accessLevel;
	}

	@Override
	public String getSuperClass() {
		return this.superClass;
	}

	@Override
	public String[] getInterfaces() {
		return this.interfaces;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	public void setInterfaces(String[] interfaces) {
		this.interfaces = interfaces;
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
			v.visit(field);
		}
		v.postFieldsVisit(this);
		for(IMethod method : this.methods){
			v.visit(method);
		}
		v.postMethodsVisit(this);
	}

	@Override
	public Collection<String> getAssociates() {
		return Collections.unmodifiableCollection(this.associates);
	}

	@Override
	public void addAssociate(String associate) {
		if (this.associates.contains(associate))
			return;
		
		if (associate.equals(this.getName()))
			return;
		
		if (associate.startsWith("java"))
			return;
		
		this.associates.add(associate);
	}

	@Override
	public Collection<String> getUses() {
		return Collections.unmodifiableCollection(this.uses);
	}

	@Override
	public void addUse(String classname) {
		if (this.associates.contains(classname) || this.associates.contains(classname.replaceAll("\\/", ".")) || this.uses.contains(classname))
			return;
		
		if (classname.equals(this.getName()))
			return;
		
		if (classname.startsWith("java"))
			return;
		
		this.uses.add(classname);
	}

}
