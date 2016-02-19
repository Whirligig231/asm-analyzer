package analyzer.gui.tree;

import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public abstract class AbstractNode extends DefaultMutableTreeNode implements TreeNode {
	
	protected abstract Iterator getChildrenIterator();
	
	 public static <T> Iterable<T> in(final Iterator<T> iterator) {
		    assert iterator != null;
		    class SingleUseIterable implements Iterable<T> {
		      private boolean used = false;

		      @Override
		      public Iterator<T> iterator() {
		        if (used) {
		          throw new IllegalStateException("SingleUseIterable already invoked");
		        }
		        used = true;
		        return iterator;
		      }
		    }
		    return new SingleUseIterable();
		  }
	 
	@Override
	public TreeNode getChildAt(int childIndex) {
		int count = 0;
		for(Object child : in(getChildrenIterator())){
			if(childIndex == count){
				if(child instanceof TreeNode){
					return (TreeNode) child;
				}else{
					return null;
				}
			}
			count++;
		}
		return null;
	}

	@Override
	public int getChildCount() {
		int count = 0;
		for(Object child : in(getChildrenIterator())){
			count++;
		}
		return count;
	}

	@Override
	public TreeNode getParent() {
		return null; //TOOD: Implement correctly
	}

	@Override
	public int getIndex(TreeNode node) {
		int count = 0;
		for(Object child : in(getChildrenIterator())){
			if(child instanceof TreeNode){
				TreeNode treeNodeChild = (TreeNode) child;
				if(treeNodeChild.equals(node)){
					return count;
				}
			}
			count++;
		}
		return -1;
	}

	@Override
	public Enumeration children() {
		System.out.println("getting enum children of node");
		return null; //TODO: Implement correctly
	}
}
