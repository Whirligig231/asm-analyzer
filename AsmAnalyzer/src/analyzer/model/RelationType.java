package analyzer.model;

import org.objectweb.asm.Opcodes;

public enum RelationType {
	EXTENDS ("extends"),
	IMPLEMENTS ("implements"),
	ASSOCIATES ("associates"),
	USES ("uses");
	
	private String strValue;
	
	RelationType(String strValue) {
		this.strValue = strValue;
	}

	@Override
	public String toString() {
		return this.strValue;
	}
}
