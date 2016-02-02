package analyzer.model.pattern;

import analyzer.model.IClass;
import analyzer.model.IRelation;
import analyzer.model.RelationType;
import analyzer.visitor.common.IVisitor;

public interface IAnnotatedRelation extends IRelation {

	public String getAnnotation();

}
