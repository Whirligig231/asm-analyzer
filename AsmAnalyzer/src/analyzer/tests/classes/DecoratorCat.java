package analyzer.tests.classes;

public class DecoratorCat implements ICat {
	
	private ICat decorated;

	public DecoratorCat(ICat decorated) {
		this.decorated = decorated;
	}

	@Override
	public void meow() {
		this.decorated.meow();
	}

}
