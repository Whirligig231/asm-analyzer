package analyzer.model.pattern;

import java.awt.Color;

public class SingletonPattern extends Pattern {

	@Override
	public Color getColor() {
		return Color.BLUE;
	}

	@Override
	public String getName() {
		return "Singleton";
	}

}
