package analyzer.visitor.detect;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IFieldStatement;
import analyzer.model.IMethod;
import analyzer.model.IMethodStatement;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.IStatement;
import analyzer.model.Relation;
import analyzer.model.RelationType;
import analyzer.model.StatementType;
import analyzer.model.pattern.ComponentClass;
import analyzer.model.pattern.DecoratesRelation;
import analyzer.model.pattern.DecoratorClass;
import analyzer.model.pattern.SingletonClass;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class DecoratorDetector {
	
	private final IVisitor visitor;
	private IModel model;
	private IClass currentClass;
	private IMethod currentMethod;
	private Map<IClass, Set<String>> methodsToFind;
	private Map<IClass, IField> interFields;
	private IClass searchClass;
	private boolean hasFS = false, hasMS = false;

	public DecoratorDetector() {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
		this.setupPostMethodsVisitClass();
		this.setupPreVisitModel();
		this.setupVisitField();
		this.setupPreVisitMethod();
		this.setupVisitStatement();
		this.setupPostVisitMethod();
	}
	
	public void detect(IModel model) {
		model.accept(this.visitor);
	}
	
	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				DecoratorDetector.this.currentClass = c;
				DecoratorDetector.this.methodsToFind = new HashMap<>();
				
				Iterator<IClass> it = c.getInterfacesIterator();
				while (it.hasNext()) {
					IClass i = it.next();
					methodsToFind.put(i, new HashSet<>());
					Iterator<IMethod> it2 = i.getMethodIterator();
					while (it2.hasNext()) {
						IMethod m = it2.next();
						methodsToFind.get(i).add(m.getName() + m.getDesc());
					}
				}
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				for (Entry<IClass, Set<String>> e : methodsToFind.entrySet()) {
					IClass c = e.getKey();
					Set<String> s = e.getValue();
					if (s.isEmpty()) {
						// c is decorated
						model.addClass(new DecoratorClass(currentClass));
						model.addClass(new ComponentClass(c));
						model.addRelation(new DecoratesRelation(
								new Relation(currentClass, c, RelationType.ASSOCIATES, model)));
					}
				}
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IClass.class, command);
	}
	
	private void setupPreVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IMethod c = (IMethod)t;
				String nameDesc = c.getName() + c.getDesc();
				for (IClass i : methodsToFind.keySet()) {
					Set<String> s = methodsToFind.get(i);
					if (s.contains(nameDesc))
						searchClass = i;
				}
				hasFS = false;
				hasMS = false;
				currentMethod = c;
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IMethod.class, command);
	}
	
	private void setupPostVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				if ((!hasFS) || (!hasMS)) {
					methodsToFind.remove(searchClass);
					interFields.remove(searchClass);
				}
				else {
					methodsToFind.get(searchClass).remove(currentMethod.getName() + currentMethod.getDesc());
				}
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IMethod.class, command);
	}
	
	private void setupVisitField() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IField c = (IField)t;
				IClass type = model.getClass(c.getType());
				if (methodsToFind.keySet().contains(type))
					interFields.put(type, c);
			}
		};
		this.visitor.addVisit(VisitType.Visit, IField.class, command);
	}

	private void setupPreVisitModel() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				DecoratorDetector.this.model = (IModel)t;
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IModel.class, command);
	}
	
	
	private void setupVisitStatement() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IStatement c = (IStatement)t;
				
				if (c instanceof IFieldStatement) {
					IFieldStatement fs = (IFieldStatement)c;
					if (interFields.get(searchClass).equals(fs.getField()))
						hasFS = true;
				}
				else if (c instanceof IMethodStatement) {
					IMethodStatement ms = (IMethodStatement)c;
					IMethod called = ms.getMethod();
					if (called.getName().equals(currentMethod.getName()) &&
							called.getDesc().equals(currentMethod.getDesc()) &&
							called.getOwner().equals(searchClass))
						hasMS = true;
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IStatement.class, command);
	}

}
