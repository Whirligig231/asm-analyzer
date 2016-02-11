package lab72.problem.sprites;

public class ConcentricSprite extends CompositeSprite {

	public ConcentricSprite(double x, double y, double width, double height) {
		this.addSprite(new CircleSprite(x, y, width, height));
		this.addSprite(new CircleSprite(x+width/4, y+width/4, width/2, height/2));
	}

}
