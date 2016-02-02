package analyzer.model.pattern;

import analyzer.model.IRelation;

public class AdaptsRelation extends AnnotatedRelation {

	public AdaptsRelation(IRelation decorated) {
		super(decorated, "adapts");
	}

}
