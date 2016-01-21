package problem.asm.test.classes;

import problem.asm.ClassNameStandardizer;
import problem.asm.model.AccessLevel;
import problem.asm.tests.Testable;

public class AccessLevelTest implements Testable {

	@Override
	public boolean test() {
		boolean testOutput = true;
		
		int publicOpcode = 9;
		int protectedOpcode = 12;
		int privateOpcode = 18;
		
		AccessLevel publicOpcodeOutput = AccessLevel.getFromOpcodes(publicOpcode);
		AccessLevel protectedOpcodeOutput = AccessLevel.getFromOpcodes(protectedOpcode);
		AccessLevel privateOpcodeOutput = AccessLevel.getFromOpcodes(privateOpcode);
		
		testOutput = testOutput || publicOpcodeOutput.equals(AccessLevel.PUBLIC);
		testOutput = testOutput || protectedOpcodeOutput.equals(AccessLevel.PROTECTED);
		testOutput = testOutput || privateOpcodeOutput.equals(AccessLevel.PRIVATE);
		return testOutput;
	}

}
