package analyzer.gui.tree;

import java.util.Iterator;

import analyzer.model.IClass;
import java.util.ArrayList;

public class ClassNode extends AbstractNode {
	
	IClass clazz;
	
	public ClassNode(IClass clazz){
		this.clazz = clazz;
	}

	@Override
	protected Iterator getChildrenIterator() {
		return new ArrayList().iterator(); //return empty iterator
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

}
