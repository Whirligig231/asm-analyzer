package analyzer.tests.classes;

public class Bat implements IBat {

	@Override
	public void squeak() {
		System.out.println("SKREEEEEEEE");
	}

	@Override
	public void fly() {
		System.out.println("flap flap flap");
	}

}
