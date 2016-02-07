package analyzer.tests.classes;

public class Computer implements IPowerDevice {
	
	public String os;

	public Computer(String os) {
		this.os = os;
	}

	@Override
	public void turnOn() {
		System.out.println("COMPUTER ON - Welcome to " + this.os);
	}

	@Override
	public void turnOff() {
		System.out.println("Computer is off");
	}

}
