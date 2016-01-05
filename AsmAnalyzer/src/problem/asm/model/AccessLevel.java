package problem.asm.model;

import org.objectweb.asm.Opcodes;

public enum AccessLevel {
	PUBLIC ("public"),
	PROTECTED ("protected"),
	DEFAULT ("default"),
	PRIVATE ("private");
	
	private String strValue;
	
	AccessLevel(String strValue) {
		this.strValue = strValue;
	}
	
	public static AccessLevel getFromOpcodes(int opcodes) {
		if ((opcodes & Opcodes.ACC_PUBLIC) != 0) {
			return AccessLevel.PUBLIC;
		}
		else if ((opcodes & Opcodes.ACC_PROTECTED) != 0) {
			return AccessLevel.PROTECTED;
		}
		else if ((opcodes & Opcodes.ACC_PRIVATE) != 0) {
			return AccessLevel.PRIVATE;
		}
		else{
			return AccessLevel.DEFAULT;
		}
	}
	
	@Override
	public String toString() {
		return this.strValue;
	}
}
