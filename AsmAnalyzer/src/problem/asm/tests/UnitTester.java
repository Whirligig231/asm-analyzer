package problem.asm.tests;

import problem.asm.test.classes.ClassNameStandardizerTest;

public class UnitTester {

	public static void main(String[] args) {
		Testable[] testingObjects = {new ClassNameStandardizerTest()};
		int testsAttempted = 0;
		int testsPassed = 0;
		
		for(Testable object : testingObjects){
			testsAttempted++;
			if(object.test())
				testsPassed++;
		}
		
		System.out.println("Tests passed: "+testsAttempted+"/"+testsPassed);
	}

}
