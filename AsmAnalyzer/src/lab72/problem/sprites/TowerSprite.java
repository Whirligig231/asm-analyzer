package lab72.problem.sprites;

public class TowerSprite extends CompositeSprite {

	public TowerSprite(double x, double y, double width, double height) {
		this.addSprite(new PyramidSprite(x, y+height/2, width, height/2));
		this.addSprite(new ConcentricSprite(x+width/4, y, width/2, height/2));
	}

}
