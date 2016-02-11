package lab72.problem.sprites;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class CompositeIterator<T extends Iterable<T>> implements Iterator<T> {
	
	private Stack<Iterator<T>> stack;
	
	public CompositeIterator(Iterator<T> it) {
		this.stack = new Stack<>();
		this.stack.push(it);
	}

	@Override
	public T next() {
		if (this.hasNext()) {
			Iterator<T> it = this.stack.peek();
			T component = it.next();
			this.stack.push(component.iterator());
			return component;
		}
		else {
			throw new NoSuchElementException();
		}
	}
	
	@Override
	public boolean hasNext() {
		if (this.stack.empty())
			return false;
		else {
			Iterator<T> it = stack.peek();
			if (!it.hasNext()) {
				this.stack.pop();
				return this.hasNext();
			}
			else {
				return true;
			}
		}
	}

}
