package analyzer.visitor.detect;

import java.util.Iterator;
import java.util.Observable;

import analyzer.model.IClass;
import analyzer.model.IModel;
import analyzer.model.pattern.DecoratorClass;
import analyzer.model.pattern.DecoratorPattern;
import analyzer.model.pattern.IPattern;
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
				
				IPattern p = getDecorator(c);
				
				if (p != null) {
					model.addClass(new DecoratorClass(c));
					p.addClass(c);
				}
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}

	private IPattern getDecorator(IClass c) {
		IClass superClass = c.getSuperClass();
		if (superClass == null)
			return null;
		if (superClass instanceof DecoratorClass) {
			Iterator<IPattern> it = model.getPatternIterator();
			while (it.hasNext()) {
				IPattern p = it.next();
				if (!(p instanceof DecoratorPattern))
					continue;
				for (IClass c2 : p) {
					if (c2.equals(superClass))
						return p;
				}
			}
		}
		return getDecorator(superClass);
	}
	

}
