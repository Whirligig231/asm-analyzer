package analyzer.visitor.detect;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IMethod;
import analyzer.model.IModel;
import analyzer.model.IStatement;
import analyzer.model.pattern.AnnotatedClass;
import analyzer.model.pattern.CompositeClass;
import analyzer.model.pattern.CompositeComponentClass;
import analyzer.model.pattern.LeafClass;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class CompositeDetector {
	
	
	private final IVisitor visitor;
	private IModel model;
	private IClass currentClass;
	private IField instanceField;
	private IClass component;
	private Set<IClass> supertypes;
	private boolean tpContainsSuper;
	private Set<IClass> compChildClasses;
	private Set<IClass> leafs = new HashSet<IClass>();

	public CompositeDetector() {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
		this.setupPostMethodsVisitClass();
		this.setupPreVisitModel();
		this.setupVisitField();
		this.setupPreVisitMethod();
		this.setupVisitStatement();
	}
	
	public void detect(IModel model) {
		model.accept(this.visitor);
	}
	
	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				CompositeDetector.this.component = null;
				CompositeDetector.this.leafs = new HashSet<IClass>();
				CompositeDetector.this.compChildClasses = new HashSet<IClass>();
				CompositeDetector.this.currentClass = c;
				CompositeDetector.this.supertypes = getSupertypes(c);
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				if (CompositeDetector.this.component != null
						&& CompositeDetector.this.currentClass != null
						&& !CompositeDetector.this.leafs.isEmpty()) {
					AnnotatedClass annotatedCompositeClass = new CompositeClass(CompositeDetector.this.currentClass);
					CompositeDetector.this.model.addClass(annotatedCompositeClass);
					AnnotatedClass annotatedComponentClass = new CompositeComponentClass(CompositeDetector.this.component);
					CompositeDetector.this.model.addClass(annotatedComponentClass);
					for(IClass leaf : CompositeDetector.this.leafs){
						AnnotatedClass annotatedLeafClass = new LeafClass(leaf);
						CompositeDetector.this.model.addClass(annotatedLeafClass);
					}
					for(IClass comp : CompositeDetector.this.compChildClasses){
						AnnotatedClass annotatedCompositeChildClass = new CompositeClass(comp);
						CompositeDetector.this.model.addClass(annotatedCompositeChildClass);
					}
					 //System.out.println(currentClass.getName() + " IS Composite! DECORATING!  Num leafs: " + CompositeDetector.this.leafs.size());
				}
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IClass.class, command);
	}
	
	private void setupPreVisitMethod() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				//TODO: implement if needed
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IMethod.class, command);
	}
	
	 public static <T> Iterable<T> in(final Iterator<T> iterator) {
	    assert iterator != null;
	    class SingleUseIterable implements Iterable<T> {
	      private boolean used = false;

	      @Override
	      public Iterator<T> iterator() {
	        if (used) {
	          throw new IllegalStateException("SingleUseIterable already invoked");
	        }
	        used = true;
	        return iterator;
	      }
	    }
	    return new SingleUseIterable();
	  }
	
	private void setupVisitField() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IField f = (IField)t;
				Iterator<String> tpIterator = f.getTypeParameterIterator();
				for(String typeParameter : in(tpIterator)){
					for(IClass superType : CompositeDetector.this.supertypes){
						if(superType.getName() == typeParameter) {
							CompositeDetector.this.component = superType; //If it matches the composite pattern, set the component
							
							//Now, loop through all classes in the model and add leafs as required
							for(IClass modelClass : in(CompositeDetector.this.model.getClassIterator())){
								Set<IClass> modelClassSupertypes = getSupertypes(modelClass);
								
								if (modelClassSupertypes.contains(superType) && 
										!modelClass.equals(CompositeDetector.this.currentClass) &&
										!getSupertypes(modelClass).contains(CompositeDetector.this.currentClass)){
									CompositeDetector.this.leafs.add(modelClass);
								}
								
								if(modelClassSupertypes.contains(CompositeDetector.this.currentClass)){
									CompositeDetector.this.compChildClasses.add(modelClass);
								}
							}
						}
					}
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IField.class, command);
	}

	private void setupPreVisitModel() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				CompositeDetector.this.model = (IModel)t;
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IModel.class, command);
	}
	
	public static boolean isCollectionOfObject(Collection c, String className){
		for(Object co : c){
			if(!(ClassNameStandardizer.standardize(co.getClass().toString()) == className))
				return false;
		}
		return true;
	}
	
	private Set<IClass> getSupertypes(IClass c) {
		Iterator<IClass> it = c.getInterfacesIterator();
		Set<IClass> supertypes = new HashSet<IClass>();
		while (it.hasNext()) {
			IClass i = it.next();
			supertypes.add(i);
			supertypes.addAll(getSupertypes(i));
		}
		if (c.getSuperClass() != null) {
			supertypes.add(c.getSuperClass());
			supertypes.addAll(getSupertypes(c.getSuperClass()));
		}
		return supertypes;
	}
	
	private void setupVisitStatement() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				//TODO: implement if needed
				/*IStatement c = (IStatement)t;
				
				if (c instanceof IFieldStatement) {
					IFieldStatement fs = (IFieldStatement)c;
					if (CompositeDetector.this.methodIsInstance
							&& fs.getType() == StatementType.GET_FIELD
							&& fs.getField() == CompositeDetector.this.instanceField)
						CompositeDetector.this.hasInstanceGetter = true;
				}*/
			}
		};
		this.visitor.addVisit(VisitType.Visit, IStatement.class, command);
	}
}
