package analyzer.visitor.detect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IModel;
import analyzer.model.pattern.AnnotatedClass;
import analyzer.model.pattern.CompositeClass;
import analyzer.model.pattern.CompositeComponentClass;
import analyzer.model.pattern.LeafClass;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class CompositePatternDetector implements IPatternDetector  {
	
	
	private final IVisitor visitor;
	private IModel model;
	private IClass currentClass;
	private IClass component;
	private Set<IClass> supertypes;
	private Set<IClass> compChildClasses;
	private Set<IClass> leafs = new HashSet<IClass>();

	public CompositePatternDetector() {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
		this.setupPostMethodsVisitClass();
		this.setupPreVisitModel();
		this.setupVisitField();
	}
	
	public void detect(IModel model) {
		model.accept(this.visitor);
	}
	
	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				CompositePatternDetector.this.component = null;
				CompositePatternDetector.this.leafs = new HashSet<IClass>();
				CompositePatternDetector.this.compChildClasses = new HashSet<IClass>();
				CompositePatternDetector.this.currentClass = c;
				CompositePatternDetector.this.supertypes = getSupertypes(c);
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				if (CompositePatternDetector.this.component != null
						&& CompositePatternDetector.this.currentClass != null
						&& !CompositePatternDetector.this.leafs.isEmpty()) {
					AnnotatedClass annotatedCompositeClass = new CompositeClass(CompositePatternDetector.this.currentClass);
					CompositePatternDetector.this.model.addClass(annotatedCompositeClass);
					AnnotatedClass annotatedComponentClass = new CompositeComponentClass(CompositePatternDetector.this.component);
					CompositePatternDetector.this.model.addClass(annotatedComponentClass);
					for(IClass leaf : CompositePatternDetector.this.leafs){
						AnnotatedClass annotatedLeafClass = new LeafClass(leaf);
						CompositePatternDetector.this.model.addClass(annotatedLeafClass);
					}
					for(IClass comp : CompositePatternDetector.this.compChildClasses){
						AnnotatedClass annotatedCompositeChildClass = new CompositeClass(comp);
						CompositePatternDetector.this.model.addClass(annotatedCompositeChildClass);
					}
					 //System.out.println(currentClass.getName() + " IS Composite! DECORATING!  Num leafs: " + CompositeDetector.this.leafs.size());
				}
			}
		};
		this.visitor.addVisit(VisitType.PostVisit, IClass.class, command);
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
				//System.out.println("Checking type parameters for " + f.getName());
				for(String typeParameter : in(tpIterator)){
					//System.out.println("Type parameter: "+typeParameter);
					for(IClass superType : CompositePatternDetector.this.supertypes){
						if(superType.getName().equals(typeParameter)) {
							//System.out.println("FOUND IT");
							CompositePatternDetector.this.component = superType; //If it matches the composite pattern, set the component
							
							//Now, loop through all classes in the model and add leafs as required
							for(IClass modelClass : in(CompositePatternDetector.this.model.getClassIterator())){
								Set<IClass> modelClassSupertypes = getSupertypes(modelClass);
								
								if (modelClassSupertypes.contains(superType) && 
										!modelClass.equals(CompositePatternDetector.this.currentClass) &&
										!getSupertypes(modelClass).contains(CompositePatternDetector.this.currentClass)){
									CompositePatternDetector.this.leafs.add(modelClass);
								}
								
								if(modelClassSupertypes.contains(CompositePatternDetector.this.currentClass)){
									CompositePatternDetector.this.compChildClasses.add(modelClass);
								}
							}
						}
						else {
							//System.out.println("We want "+superType.getName());
							//System.out.println("But this is "+typeParameter);
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
				CompositePatternDetector.this.model = (IModel)t;
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
}
