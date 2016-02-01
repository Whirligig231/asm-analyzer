package analyzer.visitor.detect;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

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
import analyzer.model.pattern.Pattern;
import analyzer.model.pattern.SingletonPattern;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class SingletonDetector {
	
	private static SingletonDetector instance;
	
	private final IVisitor visitor;
	private IModel model;
	private IClass currentClass;
	private IField instanceField;
	private boolean methodIsInstance;
	private boolean hasPrivateCtor;
	private boolean hasInstanceGetter;

	private SingletonDetector() {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
		this.setupPostMethodsVisitClass();
		this.setupPreVisitModel();
		this.setupVisitField();
		this.setupPreVisitMethod();
		this.setupVisitStatement();
	}

	public static SingletonDetector getInstance() {
		if (SingletonDetector.instance == null) {
			SingletonDetector.instance = new SingletonDetector();
		}
		
		return SingletonDetector.instance;
	}
	
	public void detect(IModel model) {
		model.accept(this.visitor);
	}
	
	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				SingletonDetector.this.currentClass = c;
				SingletonDetector.this.hasInstanceGetter = false;
				SingletonDetector.this.hasPrivateCtor = false;
				SingletonDetector.this.instanceField = null;
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}
	
	private void setupPostMethodsVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				if (SingletonDetector.this.instanceField != null
						&& SingletonDetector.this.hasPrivateCtor
						&& SingletonDetector.this.hasInstanceGetter) {
					Pattern pattern = new SingletonPattern();
					pattern.addClass(SingletonDetector.this.currentClass);
					SingletonDetector.this.model.addPattern(pattern);
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
					SingletonDetector.this.hasPrivateCtor = true;
				
				SingletonDetector.this.methodIsInstance =
						c.getAccessLevel() == AccessLevel.PUBLIC
						&& c.isStatic()
						&& ClassNameStandardizer.standardize(c.getReturnType()).equals(SingletonDetector.this.currentClass.getName());
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
						&& ClassNameStandardizer.standardize(c.getType()).equals(SingletonDetector.this.currentClass.getName()))
					SingletonDetector.this.instanceField = c;
			}
		};
		this.visitor.addVisit(VisitType.Visit, IField.class, command);
	}

	private void setupPreVisitModel() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				SingletonDetector.this.model = (IModel)t;
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
					if (SingletonDetector.this.methodIsInstance
							&& fs.getType() == StatementType.GET_FIELD
							&& fs.getField() == SingletonDetector.this.instanceField)
						SingletonDetector.this.hasInstanceGetter = true;
				}
			}
		};
		this.visitor.addVisit(VisitType.Visit, IStatement.class, command);
	}

}