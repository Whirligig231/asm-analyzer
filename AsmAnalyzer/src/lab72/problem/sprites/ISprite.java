package lab72.problem.sprites;

import java.awt.Dimension;
import java.awt.Shape;

public interface ISprite extends Iterable<ISprite> {
	public void move(Dimension space);
	public Shape getShape();
}
