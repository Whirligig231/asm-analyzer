package analyzer.visitor.detect;

import java.util.HashSet;

import analyzer.model.IClass;
import analyzer.model.IModel;
import analyzer.model.pattern.ClassClass;
import analyzer.model.pattern.ClassPattern;
import analyzer.model.pattern.IPattern;
import analyzer.visitor.common.ITraverser;
import analyzer.visitor.common.IVisitMethod;
import analyzer.visitor.common.IVisitor;
import analyzer.visitor.common.VisitType;
import analyzer.visitor.common.Visitor;

public class ClassPatternDetector extends ObservablePatternDetector {

	private final IVisitor visitor;
	private IModel model;
	
	public ClassPatternDetector() {
		this.visitor = new Visitor();
		this.setupPreVisitClass();
	}
	
	@Override
	public void detect(IModel model) {
		this.model = model;
		model.accept(this.visitor);
	}
	

	private void setupPreVisitClass() {
		IVisitMethod command = new IVisitMethod() {
			@Override
			public void execute(ITraverser t) {
				IClass c = (IClass)t;
				if (c.getName().contains("Class")) {
					IClass annotatedClass = new ClassClass(c);
					model.addClass(annotatedClass);
					
					IPattern classPattern = new ClassPattern();
					classPattern.addClass(annotatedClass);
					model.addPattern(classPattern);
				}
			}
		};
		this.visitor.addVisit(VisitType.PreVisit, IClass.class, command);
	}

}
