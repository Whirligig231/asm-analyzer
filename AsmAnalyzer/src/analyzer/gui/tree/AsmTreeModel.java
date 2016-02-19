package analyzer.gui.tree;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import analyzer.model.IClass;
import analyzer.model.IModel;
import analyzer.model.pattern.IPattern;

public class AsmTreeModel implements TreeModel {

	IModel model;

	private Vector vector = new Vector();
	
	public AsmTreeModel(IModel model){
		this.model = model;
	}
	
	@Override
	public Object getRoot() {
		return model;
	}
	
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
	public Object getChild(Object parent, int index) {
		int count = 0;
		if(parent instanceof IPattern){ 
			IPattern patternParent = (IPattern) parent;
			for(IClass classPattern : in(patternParent.iterator())){
				if (count == index){
					return classPattern;
				}
				count++;
			}
		}else if(parent instanceof IModel){
			for(IPattern pattern : in(model.getPatternIterator())){
				if(count == index){
					return pattern;
				}
				count++;
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		int count = 0;
		if(parent instanceof IPattern){ 
			IPattern patternParent = (IPattern) parent;
			for(IClass classPattern : in(patternParent.iterator())){
				count++;
			}
		}else if(parent instanceof IModel){
			for(IPattern pattern : in(model.getPatternIterator())){
				count++;
			}
		}
		return count;
	}

	@Override
	public boolean isLeaf(Object node) {
		if(node instanceof IClass){
			return true;
		}
		return false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Implement if needed (can probably be left empty)
		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		int count = 0;
		if(parent instanceof IPattern && child instanceof IClass){ 
			IPattern patternParent = (IPattern) parent;
			IClass classChild = (IClass) child;
			for(IClass classPattern : in(patternParent.iterator())){
				if(classPattern.equals(classChild))
					return count;
				count++;
			}
		}else if(parent instanceof IModel && child instanceof IPattern){
			IPattern patternChild = (IPattern) child;
			for(IPattern pattern : in(model.getPatternIterator())){
				if(pattern.equals(patternChild))
					return count;
				count++;
			}
		}
		return -1;
	}

   public void addTreeModelListener( TreeModelListener listener ) {
      if ( listener != null && !vector.contains( listener ) ) {
         vector.addElement( listener );
      }
   }

   public void removeTreeModelListener( TreeModelListener listener ) {
      if ( listener != null ) {
         vector.removeElement( listener );
      }
   }

}
