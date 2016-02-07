package analyzer.tests.classes;

import java.util.HashMap;
import java.util.Map;

public class PowerStrip implements IPowerDevice {
	
	private Map<Integer, IPowerDevice> devices;

	public PowerStrip() {
		this.devices = new HashMap<>();
	}
	
	public void addDevice(int k, IPowerDevice device) {
		this.devices.put(k, device);
	}
	
	public void removeDevice(int k) {
		this.devices.remove(k);
	}

	@Override
	public void turnOn() {
		System.out.println("Turning on power strip");
	}

	@Override
	public void turnOff() {
		System.out.println("Turning off power strip");
		for (IPowerDevice device : this.devices.values())
			device.turnOff();
	}

}
