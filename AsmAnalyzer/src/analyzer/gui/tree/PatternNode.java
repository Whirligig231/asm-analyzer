package analyzer.gui.tree;

import java.util.ArrayList;
import java.util.Iterator;

import analyzer.model.pattern.IPattern;

public class PatternNode extends AbstractNode {
	IPattern pattern;
	
	public PatternNode(IPattern pattern){
		this.pattern = pattern;
	}

	@Override
	protected Iterator getChildrenIterator() {
		return this.pattern.iterator();
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
}
