package analyzer.model.pattern;

import java.awt.Color;
import java.util.Iterator;

import analyzer.model.AccessLevel;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IMethod;
import analyzer.model.IModel;
import analyzer.visitor.common.IVisitor;

public class AnnotatedClass implements IAnnotatedClass {
	
	private IClass decorated;
	private String annotation;
	private Color color;

	public AnnotatedClass(IClass decorated, String annotation, Color color) {
		this.decorated = decorated;
		this.annotation = annotation;
		this.color = color;
	}

	@Override
	public String getAnnotation() {
		return this.annotation;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public String getName() {
		return this.decorated.getName();
	}

	@Override
	public AccessLevel getAccessLevel() {
		return this.decorated.getAccessLevel();
	}

	@Override
	public void setName(String name) {
		this.decorated.setName(name);
	}

	@Override
	public void setAccessLevel(AccessLevel accessLevel) {
		this.decorated.setAccessLevel(accessLevel);
	}

	@Override
	public IModel getOwner() {
		return this.decorated.getOwner();
	}

	@Override
	public void setOwner(IModel model) {
		this.decorated.setOwner(model);
	}

	@Override
	public Iterator<IMethod> getMethodIterator() {
		return this.decorated.getMethodIterator();
	}

	@Override
	public void addMethod(IMethod method) {
		this.decorated.addMethod(method);
	}

	@Override
	public IMethod getMethod(String name, String desc) {
		return this.decorated.getMethod(name, desc);
	}

	@Override
	public Iterator<IField> getFieldIterator() {
		return this.decorated.getFieldIterator();
	}

	@Override
	public void addField(IField field) {
		this.decorated.addField(field);
	}

	@Override
	public IField getField(String name, String type) {
		return this.decorated.getField(name, type);
	}

	@Override
	public void accept(IVisitor v) {
		this.decorated.accept(v);
	}

	@Override
	public IClass getSuperClass() {
		return this.decorated.getSuperClass();
	}

	@Override
	public void setSuperClass(IClass superClass) {
		this.decorated.setSuperClass(superClass);
	}

	@Override
	public Iterator<IClass> getInterfacesIterator() {
		return this.decorated.getInterfacesIterator();
	}

	@Override
	public void addInterface(IClass myInterface) {
		this.decorated.addInterface(myInterface);
	}

}
