package analyzer.model.pattern;

import analyzer.model.IRelation;

public class DecoratesRelation extends AnnotatedRelation {

	public DecoratesRelation(IRelation decorated) {
		super(decorated, "decorates");
	}

}
