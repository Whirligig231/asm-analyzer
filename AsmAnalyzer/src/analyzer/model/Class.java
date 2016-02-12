package analyzer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import analyzer.visitor.common.IVisitor;

public class Class implements IClass {
	
	private String name;
	private AccessLevel accessLevel;
	private IModel model;
	private String superClass;
	private Collection<String> interfaces;
	
	private Map<String, IMethod> methods;
	private Map<String, IField> fields;
	
	public Class() {
		this.methods = new HashMap<>();
		this.fields = new HashMap<>();
		this.interfaces = new ArrayList<>();
	}
	
	@Override
	public boolean equals(Object o){ //TODO: Fix this incredibly dirty implementation that Steven implemented because he's lazy af
		if(o instanceof Class){
			Class oClass = (Class) o;
			return this.name.equals(oClass.name) && 
					((this.superClass == null && oClass.superClass == null) ||
					this.superClass.equals(oClass.superClass)) &&
					this.accessLevel == oClass.accessLevel;
		}else{
			return false;
		}
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
		//System.out.println(field.getName() + ": " + field.getType() + " added");
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

	@Override
	public IClass getSuperClass() {
		return this.model.getClass(this.superClass);
	}

	@Override
	public void setSuperClass(IClass superClass) {
		this.superClass = superClass.getName();
	}

	@Override
	public Iterator<IClass> getInterfacesIterator() {
		return new ClassModelIterator(this.interfaces.iterator(), this.model);
	}

	@Override
	public void addInterface(IClass myInterface) {
		this.interfaces.add(myInterface.getName());
	}

}
