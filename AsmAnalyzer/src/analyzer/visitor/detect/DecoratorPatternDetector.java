package analyzer.visitor.detect;

public class DecoratorPatternDetector extends CompositeDetector {
	
	public DecoratorPatternDetector(int threshold) {
		super();
		this.addDetector(new DecoratorPatternComponentDetector(threshold));
		this.addDetector(new DecoratorPatternSubclassDetector());
	}

}
