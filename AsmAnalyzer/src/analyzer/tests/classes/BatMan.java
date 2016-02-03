package analyzer.tests.classes;

public class BatMan implements IBat {
	
	private IMan man;

	public BatMan(IMan man) {
		this.man = man;
	}

	@Override
	public void squeak() {
		this.man.speak();
	}

	@Override
	public void fly() {
		System.out.println("I'm using a grappling hook");
	}

}
