package analyzer.model;

import java.util.Iterator;

public class ClassModelIterator implements Iterator<IClass> {
	
	private Iterator<String> it;
	private IModel model;

	public ClassModelIterator(Iterator<String> it, IModel model) {
		this.it = it;
		this.model = model;
	}

	@Override
	public boolean hasNext() {
		return this.it.hasNext();
	}

	@Override
	public IClass next() {
		if (this.it.next() == null)
			return null;
		return model.getClass(this.it.next());
	}

}
