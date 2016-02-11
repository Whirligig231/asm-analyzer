package lab72.problem.sprites;

import java.awt.Dimension;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CompositeSprite implements ISprite {

	private Collection<ISprite> sprites;
	
	public CompositeSprite() {
		this.sprites = new ArrayList<ISprite>();
	}

	@Override
	public Iterator<ISprite> iterator() {
		return new CompositeIterator<ISprite>(this.sprites.iterator());
	}

	@Override
	public void move(Dimension space) {
		for (ISprite sprite : this.sprites)
			sprite.move(space);
	}

	@Override
	public Shape getShape() {
		return null;
	}
	
	public void addSprite(ISprite sprite) {
		this.sprites.add(sprite);
	}

}
