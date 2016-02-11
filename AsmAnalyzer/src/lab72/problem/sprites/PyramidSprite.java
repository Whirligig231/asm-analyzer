package lab72.problem.sprites;

public class PyramidSprite extends CompositeSprite {

	public PyramidSprite(double x, double y, double width, double height) {
		this.addSprite(new RectangleSprite(x, y+(height*3/4), width, height/4));
		this.addSprite(new RectangleSprite(x+(width*1/8), y+(height/2), width*3/4, height/4));
		this.addSprite(new RectangleSprite(x+(width/4), y+(height/4), width/2, height/4));
		this.addSprite(new RectangleSprite(x+(width*3/8), y, width/4, height/4));
	}

}
