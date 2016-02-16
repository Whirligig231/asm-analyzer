package analyzer.visitor.detect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IFieldStatement;
import analyzer.model.IMethod;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.Relation;
import analyzer.model.RelationType;
import analyzer.model.pattern.AdapteeClass;
import analyzer.model.pattern.AdapterClass;
import analyzer.model.pattern.AdaptsRelation;
import analyzer.model.pattern.TargetClass;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class AdapterPatternDetector extends ObservablePatternDetector  {
	
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
		public String toString() {
			return "FieldClassPair [field=" + field.getName() + ", supertype=" + supertype.getName() + "]";
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
	private int threshold;
	private Map<FieldClassPair, Integer> badMethods;
	private Map<IField, Boolean> hasFS;

	public AdapterPatternDetector(int threshold) {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
		this.setupVisitField();
		this.setupPreVisitMethod();
		this.setupVisitFieldStatement();
		this.setupPostVisitMethod();
		this.setupPostMethodsVisitClass();
		
		this.threshold = threshold;
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
				classVisitUpdate(c.getName());
				// System.out.println("\n\n\n\nNOW IN CLASS: "+c.getName()+"\n-----------\n\n\n\n");
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
				// System.out.println("Add field "+f.getName());
				for (IClass i : supertypes) {
					FieldClassPair fcp = new FieldClassPair(f, i);
					// System.out.println("Adding " + fcp);
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
				searchClass = null;
				for (IClass i : supertypes) {
					if (i.getMethod(m.getName(), m.getDesc()) != null) {
						// System.out.println("Search class is "+i.getName());
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

				for (IField f : fields) {
					boolean success = hasFS.get(f);
					for (IClass c : supertypes) {
						FieldClassPair fcp = new FieldClassPair(f, c);
						// System.out.println("Getting "+fcp);
						int currentGood = goodMethods.get(fcp);
						int currentBad = badMethods.get(fcp);
						Set<String> s = methodsToFind.get(fcp);
						s.remove(m.getName() + m.getDesc());
						if (success && c.equals(searchClass)) {
							goodMethods.put(fcp, currentGood + 1);
						}
						else if (!m.getName().equals("<init>")) {
							badMethods.put(fcp, currentBad + 1);
						}
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
					if (fcp.getField().getType().equals(fcp.getSupertype().getName()))
						continue;
					int good = goodMethods.get(fcp);
					int bad = badMethods.get(fcp) + methodsToFind.get(fcp).size();
					// System.out.println(good+" "+bad);
					if (good >= threshold) {
						// it's an adapter!
						IClass adapter = c;
						model.addClass(new AdapterClass(adapter));
						IClass target = fcp.getSupertype();
						model.addClass(new TargetClass(target));
						IClass adaptee = model.getClass(fcp.getField().getType());
						if (adaptee != null) {
							// System.out.println(adapter.getName() + " adapts " + adaptee.getName() + " " + fcp.getField().getName() + " for " + target.getName());
							model.addClass(new AdapteeClass(adaptee));
							IRelation adapts = new Relation(adapter, adaptee, RelationType.ASSOCIATES, model);
							model.addRelation(new AdaptsRelation(adapts));
						}
						// else
							// System.out.println(adapter.getName() + " adapts for " + target.getName());
					}
				}
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IClass.class, command);
	}
	

}
