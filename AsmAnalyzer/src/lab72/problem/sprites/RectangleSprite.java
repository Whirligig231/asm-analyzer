package lab72.problem.sprites;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

public class RectangleSprite extends AbstractSprite {

	public RectangleSprite(double x, double y, double width, double height) {
		super(x, y, width, height);
		shape = new Rectangle2D.Double(x, y, width, height);
	}

	@Override
	public void move(Dimension space) {
		Rectangle2D bounds = this.computeNewBoundsAfterMoving(space);
		shape = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}
}
