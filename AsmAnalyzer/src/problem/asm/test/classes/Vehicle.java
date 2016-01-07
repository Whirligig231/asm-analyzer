package problem.asm.test.classes;

public class Vehicle implements Drivable {
	Driver driver;
	
	public Vehicle(Driver driver){
		this.driver = driver;
	}
	
	public void setDriver(Driver driver){
		this.driver = driver;
	}
	
	public Driver getDriver(){
		return this.driver;
	}
}
