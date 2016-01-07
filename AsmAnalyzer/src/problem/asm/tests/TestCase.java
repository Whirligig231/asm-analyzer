package problem.asm.tests;

public class TestCase {
	public String[] classes;
	public String expectedCodeFilePath;
	
	public TestCase(String[] classes, String expectedCodeFilePath){
		this.classes = classes;
		this.expectedCodeFilePath = expectedCodeFilePath;
	}
}
