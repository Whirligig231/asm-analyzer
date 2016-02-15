package analyzer.visitor.detect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IFieldStatement;
import analyzer.model.IMethod;
import analyzer.model.IMethodStatement;
import analyzer.model.IModel;
import analyzer.model.IStatement;
import analyzer.model.Relation;
import analyzer.model.RelationType;
import analyzer.model.pattern.ComponentClass;
import analyzer.model.pattern.DecoratesRelation;
import analyzer.model.pattern.DecoratorClass;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class DecoratorPatternComponentDetector implements IPatternDetector  {
	
	private final IVisitor visitor;
	private IModel model;
	private IClass currentClass;
	private IMethod currentMethod;
	private Map<IClass, Set<String>> methodsToFind;
	private Map<IClass, Integer> goodMethods;
	private int threshold;
	private Map<IClass, Integer> badMethods;
	private Map<IClass, IField> interFields;
	private IClass searchClass;
	private boolean hasFS = false, hasMS = false;

	public DecoratorPatternComponentDetector(int threshold) {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
		this.setupPostMethodsVisitClass();
		this.setupPreVisitModel();
		this.setupVisitField();
		this.setupPreVisitMethod();
		this.setupVisitStatement();
		this.setupPostVisitMethod();
		
		this.threshold = threshold;
	}
	
	public void detect(IModel model) {
		model.accept(this.visitor);
	}
	
	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				// System.out.println("WE ARE NOW VISITING: "+c.getName());
				DecoratorPatternComponentDetector.this.currentClass = c;
				DecoratorPatternComponentDetector.this.methodsToFind = new HashMap<>();
				DecoratorPatternComponentDetector.this.interFields = new HashMap<>();
				DecoratorPatternComponentDetector.this.goodMethods = new HashMap<>();
				DecoratorPatternComponentDetector.this.badMethods = new HashMap<>();
				
				DecoratorPatternComponentDetector.this.addInterfaces(c);
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void addInterfaces(IClass c) {
		Iterator<IClass> it = c.getInterfacesIterator();
		while (it.hasNext()) {
			IClass i = it.next();
			addInterface(i);
			addInterfaces(i);
		}
		if (c.getSuperClass() != null) {
			addInterface(c.getSuperClass());
			addInterfaces(c.getSuperClass());
		}
	}
	
	private void addInterface(IClass i) {
		// System.out.println("In supertype " + i.getName());
		methodsToFind.put(i, new HashSet<>());
		goodMethods.put(i, 0);
		badMethods.put(i, 0);
		Iterator<IMethod> it2 = i.getMethodIterator();
		while (it2.hasNext()) {
			IMethod m = it2.next();
			if (m.getName().equals("<init>"))
				continue;
			methodsToFind.get(i).add(m.getName() + m.getDesc());
			// System.out.println("We have to find " + m.getName() + m.getDesc());
		}
	}

	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				for (Entry<IClass, Set<String>> e : methodsToFind.entrySet()) {
					IClass c = e.getKey();
					Set<String> s = e.getValue();
					int good, bad;
					
					good = goodMethods.get(c);
					bad = badMethods.get(c) + s.size();
					
					if (good >= threshold) {
						// c is decorated
						// System.out.println(currentClass.getName()+" decorates "+c.getName()+" (" + good + "/" + bad + ")");
						model.addClass(new DecoratorClass(currentClass));
						model.addClass(new ComponentClass(c));
						Relation r = new Relation(currentClass, c, RelationType.ASSOCIATES, model);
						DecoratesRelation dr = new DecoratesRelation(r);
						model.addRelation(dr);
						
					}
					else {
						Iterator<String> it = s.iterator();
						// System.out.println(currentClass.getName()+" can't decorate "+c.getName()+" (" + good + "/" + bad + ")");
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
				searchClass = null;
				for (IClass i : methodsToFind.keySet()) {
					Set<String> s = methodsToFind.get(i);
					if (s.contains(nameDesc)) {
						searchClass = i;
						// System.out.println("Search for "+i.getName()+" in "+c.getOwner().getName()+"."+c.getName()+c.getDesc());
					}
					else {
						// System.out.println("Skip "+c.getOwner().getName()+"."+c.getName()+c.getDesc());
					}

				}
				// System.out.println("PREVISIT "+c.getName());
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
				IMethod c = (IMethod)t;
				// System.out.println("POSTVISIT "+c.getName());
				if ((!hasFS) || (!hasMS)) {
					// System.out.println("It wasn't successful!");
					if (searchClass != null)
						badMethods.put(searchClass, badMethods.get(searchClass) + 1);
				}
				else if (searchClass != null) {
					// System.out.println("We found it!");
					goodMethods.put(searchClass, goodMethods.get(searchClass) + 1);
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
				// System.out.println(c.getName() + " is a " + c.getType());
				if (type == null)
					return;
				// System.out.println("What about "+type.getName()+"?");
				if (methodsToFind.keySet().contains(type)) {
					// System.out.println("Found the field "+c.getName()+" for "+type.getName());
					interFields.put(type, c);
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IField.class, command);
	}

	private void setupPreVisitModel() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				DecoratorPatternComponentDetector.this.model = (IModel)t;
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
					// System.out.println("Field statement: "+fs.getField().getName());
					if (fs.getField().equals(interFields.get(searchClass))) {
						hasFS = true;
						// System.out.println("It's a good field!");
					}
					else {
						if (searchClass != null) {
							// System.out.println("Bad field, we wanted "+searchClass.getName());
						}
						// System.out.println("But this is "+fs.getField().getType());
					}

				}
				else if (c instanceof IMethodStatement) {
					IMethodStatement ms = (IMethodStatement)c;
					// System.out.println("Method statement: "+ms.getMethod().getName());
					IMethod called = ms.getMethod();
					if (called.getName().equals(currentMethod.getName()) &&
							called.getDesc().equals(currentMethod.getDesc()) &&
							called.getOwner().equals(searchClass)) {
						hasMS = true;
						// System.out.println("It's a good method!");
					}
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IStatement.class, command);
	}
	
	

}
