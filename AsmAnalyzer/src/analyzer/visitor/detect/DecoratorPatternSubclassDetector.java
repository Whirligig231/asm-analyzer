package analyzer.visitor.detect;

import java.util.Observable;

import analyzer.model.IClass;
import analyzer.model.IModel;
import analyzer.model.pattern.DecoratorClass;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class DecoratorPatternSubclassDetector extends ObservablePatternDetector  {
	
	private final IVisitor visitor;
	private IModel model;

	public DecoratorPatternSubclassDetector() {
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
				classVisitUpdate(c.getName());
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
