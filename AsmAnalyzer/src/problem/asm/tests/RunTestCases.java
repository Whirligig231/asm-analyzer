package problem.asm.tests;

public class RunTestCases {

	public static void main(String[] args) {
		Tester tester = new Tester();
		
		String[] classes1 = {"problem.asm.test.classes.Driver", "problem.asm.test.classes.Vehicle"};
		TestCase tc1 = new TestCase(classes1, "testExpectedOutputs/test1.dot");
		tester.runTest(tc1);
		
		String[] classes2 = {"problem.asm.test.classes.Car", "problem.asm.test.classes.Engine"};
		TestCase tc2 = new TestCase(classes2, "testExpectedOutputs/test2.dot");
		tester.runTest(tc2);
		
		String[] classes3 = {"problem.asm.test.classes.Car", "problem.asm.test.classes.Drivable", "problem.asm.test.classes.Driver", "problem.asm.test.classes.Engine", "problem.asm.test.classes.Vehicle"};
		TestCase tc3 = new TestCase(classes3, "testExpectedOutputs/test3.dot");
		tester.runTest(tc3);
		
		tester.printTestStatus();
	}

}
