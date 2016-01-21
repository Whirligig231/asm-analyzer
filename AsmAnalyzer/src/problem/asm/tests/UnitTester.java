package problem.asm.tests;

import problem.asm.test.classes.*;

public class UnitTester {

	public static void main(String[] args) {
		Testable[] testingObjects = {new ClassNameStandardizerTest(), new AccessLevelTest(), new ClassUmlOutputStreamTest()};
		int testsAttempted = 0;
		int testsPassed = 0;
		
		for(Testable object : testingObjects){
			testsAttempted++;
			if(object.test())
				testsPassed++;
		}
		
		System.out.println("Tests passed: "+testsPassed+"/"+testsAttempted);
	}

}
