package analyzer.visitor.detect;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IFieldStatement;
import analyzer.model.IMethod;
import analyzer.model.IModel;
import analyzer.model.IStatement;
import analyzer.model.StatementType;
import analyzer.model.pattern.SingletonClass;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class SingletonPatternDetector implements IPatternDetector {
	
	private final IVisitor visitor;
	private IModel model;
	private IClass currentClass;
	private IField instanceField;
	private boolean methodIsInstance;
	private boolean hasPrivateCtor;
	private boolean hasInstanceGetter;

	public SingletonPatternDetector() {
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
				SingletonPatternDetector.this.currentClass = c;
				SingletonPatternDetector.this.hasInstanceGetter = false;
				SingletonPatternDetector.this.hasPrivateCtor = false;
				SingletonPatternDetector.this.instanceField = null;
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				if (SingletonPatternDetector.this.instanceField != null
						&& SingletonPatternDetector.this.hasPrivateCtor
						&& SingletonPatternDetector.this.hasInstanceGetter) {
					IClass annotated = new SingletonClass(SingletonPatternDetector.this.currentClass);
					SingletonPatternDetector.this.model.addClass(annotated);
					// System.out.println(currentClass.getName() + " IS A SINGLETON! DECORATING!");
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
				if (c.getAccessLevel() == AccessLevel.PRIVATE
						&& c.getName().equals("<init>"))
					SingletonPatternDetector.this.hasPrivateCtor = true;
				
				SingletonPatternDetector.this.methodIsInstance =
						c.getAccessLevel() == AccessLevel.PUBLIC
						&& c.isStatic()
						&& ClassNameStandardizer.standardize(c.getReturnType()).equals(SingletonPatternDetector.this.currentClass.getName());
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IMethod.class, command);
	}
	
	private void setupVisitField() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IField c = (IField)t;
				if (c.isStatic()
						&& c.getAccessLevel() == AccessLevel.PRIVATE
						&& ClassNameStandardizer.standardize(c.getType()).equals(SingletonPatternDetector.this.currentClass.getName()))
					SingletonPatternDetector.this.instanceField = c;
			}
		};
		this.visitor.addVisit(VisitType.Visit, IField.class, command);
	}

	private void setupPreVisitModel() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				SingletonPatternDetector.this.model = (IModel)t;
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
					if (SingletonPatternDetector.this.methodIsInstance
							&& fs.getType() == StatementType.GET_FIELD
							&& fs.getField() == SingletonPatternDetector.this.instanceField)
						SingletonPatternDetector.this.hasInstanceGetter = true;
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IStatement.class, command);
	}

}
