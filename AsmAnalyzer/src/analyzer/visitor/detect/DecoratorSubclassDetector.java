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

public class DecoratorSubclassDetector {
	
	private final IVisitor visitor;
	private IModel model;

	public DecoratorSubclassDetector() {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
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
				if (isDecorator(c))
					model.addClass(new DecoratorClass(c));
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}

	private boolean isDecorator(IClass c) {
		IClass superClass = c.getSuperClass();
		if (superClass == null)
			return false;
		if (superClass instanceof DecoratorClass)
			return true;
		return isDecorator(superClass);
	}
	

}
