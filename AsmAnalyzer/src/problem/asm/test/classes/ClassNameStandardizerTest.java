package problem.asm.test.classes;

import problem.asm.ClassNameStandardizer;
import problem.asm.tests.Testable;

public class ClassNameStandardizerTest implements Testable {

	@Override
	public boolean test(){
		boolean testOutput = true;
		
		String nullString = null;
		String nullStringExpectedOutput = null;
		String nullStringOutput = ClassNameStandardizer.forASM(nullString);
		testOutput = testOutput || nullStringOutput.equals(nullStringExpectedOutput);
		
		
		String testString = "foo/bar$world.";
		String testStringExpectedOutput = "foo_bar_world_";
		String testStringOutput = ClassNameStandardizer.forASM(testString);
		testOutput = testOutput || testStringOutput.equals(testStringExpectedOutput);
		
		
		return testOutput;
	}
}
