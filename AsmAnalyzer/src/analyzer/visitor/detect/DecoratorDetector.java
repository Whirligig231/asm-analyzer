package analyzer.visitor.detect;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import analyzer.common.ClassNameStandardizer;
import analyzer.model.AccessLevel;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IFieldStatement;
import analyzer.model.IMethod;
import analyzer.model.IModel;
import analyzer.model.IRelation;
import analyzer.model.IStatement;
import analyzer.model.StatementType;
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
	private Map<IClass, Set<IClass>> possibleDecorated;

	public DecoratorDetector() {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
		this.setupPostMethodsVisitClass();
		this.setupPreVisitModel();
		this.setupVisitField();
		this.setupPreVisitMethod();
		this.setupVisitStatement();
	}
	
	public void detect(IModel model) {
		this.possibleDecorated = new HashMap<>();
		model.accept(this.visitor);
	}
	
	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				DecoratorDetector.this.currentClass = c;
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				if (DecoratorDetector.this.instanceField != null
						&& DecoratorDetector.this.hasPrivateCtor
						&& DecoratorDetector.this.hasInstanceGetter) {
					IClass annotated = new SingletonClass(DecoratorDetector.this.currentClass);
					DecoratorDetector.this.model.addClass(annotated);
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
					DecoratorDetector.this.hasPrivateCtor = true;
				
				DecoratorDetector.this.methodIsInstance =
						c.getAccessLevel() == AccessLevel.PUBLIC
						&& c.isStatic()
						&& ClassNameStandardizer.standardize(c.getReturnType()).equals(DecoratorDetector.this.currentClass.getName());
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IMethod.class, command);
	}
	
	private void setupVisitField() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IField c = (IField)t;
				Map<IClass, Set<IClass>> pd = DecoratorDetector.this.possibleDecorated;
				if (pd.get(DecoratorDetector.this.currentClass) == null)
					pd.put(currentClass, new HashSet<IClass>());
				
				pd.get(currentClass).add(model.getClass(c.getName()));
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
					if (DecoratorDetector.this.methodIsInstance
							&& fs.getType() == StatementType.GET_FIELD
							&& fs.getField() == DecoratorDetector.this.instanceField)
						DecoratorDetector.this.hasInstanceGetter = true;
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IStatement.class, command);
	}

}