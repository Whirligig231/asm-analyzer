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
import analyzer.model.pattern.AdapteeClass;
import analyzer.model.pattern.AdapterClass;
import analyzer.model.pattern.AdaptsRelation;
import analyzer.model.pattern.ComponentClass;
import analyzer.model.pattern.DecoratesRelation;
import analyzer.model.pattern.DecoratorClass;
import analyzer.model.pattern.SingletonClass;
import analyzer.model.pattern.TargetClass;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class AdapterDetector {
	
	private static final class FieldClassPair {
		
		private final IField field;
		private final IClass supertype;
		
		public FieldClassPair(IField field, IClass supertype) {
			this.field = field;
			this.supertype = supertype;
		}
		
		public IField getField() {
			return this.field;
		}
		
		public IClass getSupertype() {
			return this.supertype;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((field == null) ? 0 : field.hashCode());
			result = prime * result + ((supertype == null) ? 0 : supertype.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FieldClassPair other = (FieldClassPair) obj;
			if (field == null) {
				if (other.field != null)
					return false;
			} else if (!field.equals(other.field))
				return false;
			if (supertype == null) {
				if (other.supertype != null)
					return false;
			} else if (!supertype.equals(other.supertype))
				return false;
			return true;
		}
		
	}
	
	private final IVisitor visitor;
	private IModel model;
	private IClass currentClass;
	private IClass searchClass;
	private Set<IClass> supertypes;
	private Set<IField> fields;
	private Map<FieldClassPair, Set<String>> methodsToFind;
	private Map<FieldClassPair, Integer> goodMethods;
	private Map<FieldClassPair, Integer> badMethods;
	private Map<IField, Boolean> hasFS;

	public AdapterDetector() {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
		this.setupVisitField();
		this.setupPreVisitMethod();
		this.setupVisitFieldStatement();
		this.setupPostVisitMethod();
		this.setupPostMethodsVisitClass();
	}
	
	public void detect(IModel model) {
		this.model = model;
		model.accept(this.visitor);
	}
	
	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				currentClass = c;
				supertypes = new HashSet<>();
				loadSupertypes(c);
				fields = new HashSet<>();
				methodsToFind = new HashMap<>();
				goodMethods = new HashMap<>();
				badMethods = new HashMap<>();
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void loadSupertypes(IClass c) {
		Iterator<IClass> it = c.getInterfacesIterator();
		while (it.hasNext()) {
			IClass i = it.next();
			supertypes.add(i);
			loadSupertypes(i);
		}
		if (c.getSuperClass() != null) {
			supertypes.add(c.getSuperClass());
			loadSupertypes(c.getSuperClass());
		}
	}
	
	private void setupVisitField() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IField f = (IField)t;
				if (f.isStatic())
					return;
				fields.add(f);
				for (IClass i : supertypes) {
					FieldClassPair fcp = new FieldClassPair(f, i);
					Set<String> methods = new HashSet<>();
					Iterator<IMethod> it = i.getMethodIterator();
					while (it.hasNext()) {
						IMethod m = it.next();
						methods.add(m.getName() + m.getDesc());
					}
					methodsToFind.put(fcp, methods);
					goodMethods.put(fcp, 0);
					badMethods.put(fcp, 0);
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IField.class, command);
	}
	
	private void setupPreVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IMethod m = (IMethod)t;
				for (IClass i : supertypes) {
					if (i.getMethod(m.getName(), m.getDesc()) != null) {
						searchClass = i;
					}
				}
				hasFS = new HashMap<>();
				for (IField f : fields) {
					hasFS.put(f, false);
				}
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IMethod.class, command);
	}
	
	private void setupVisitFieldStatement() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IFieldStatement fs = (IFieldStatement)t;
				for (IField f : fields) {
					if (f.equals(fs.getField())) {
						hasFS.put(f, true);
					}
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IFieldStatement.class, command);
	}
	
	private void setupPostVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IMethod m = (IMethod)t;
				if (searchClass == null)
					return;
				
				for (IField f : fields) {
					boolean success = hasFS.get(f);
					FieldClassPair fcp = new FieldClassPair(f, searchClass);
					int currentGood = goodMethods.get(fcp);
					int currentBad = badMethods.get(fcp);
					Set<String> s = methodsToFind.get(fcp);
					s.remove(m.getName() + m.getDesc());
					if (success) {
						goodMethods.put(fcp, currentGood + 1);
					}
					else {
						badMethods.put(fcp, currentBad + 1);
					}
				}
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IMethod.class, command);
	}
	
	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				for (FieldClassPair fcp : methodsToFind.keySet()) {
					int good = goodMethods.get(fcp);
					int bad = badMethods.get(fcp) + methodsToFind.get(fcp).size();
					if (good >= bad && good > 0) {
						// it's an adapter!
						IClass adapter = c;
						model.addClass(new AdapterClass(adapter));
						IClass target = fcp.getSupertype();
						model.addClass(new TargetClass(target));
						IClass adaptee = fcp.getField().getOwner();
						if (adaptee != null) {
							model.addClass(new AdapteeClass(adaptee));
							IRelation adapts = new Relation(adapter, adaptee, RelationType.ASSOCIATES, model);
							model.addRelation(new AdaptsRelation(adapts));
						}
					}
				}
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IClass.class, command);
	}
	

}
