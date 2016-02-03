package analyzer.tests.classes;

public class TabbyCat extends DecoratorCat {

	public TabbyCat(ICat decorated) {
		super(decorated);
	}
	
	public String getStripes() {
		return "Yes, there are some";
	}

}
