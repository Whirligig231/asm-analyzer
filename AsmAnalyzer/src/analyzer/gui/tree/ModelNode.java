package analyzer.gui.tree;

import java.util.Iterator;

import analyzer.model.IModel;

public class ModelNode extends AbstractNode {
	IModel model;
	
	public ModelNode(IModel model){
		this.model = model;
	}

	@Override
	protected Iterator getChildrenIterator() {
		return this.model.getPatternIterator();
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
