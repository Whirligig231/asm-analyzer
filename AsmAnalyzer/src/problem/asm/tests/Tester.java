package problem.asm.tests;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import problem.asm.DesignParser;

public class Tester {
	public int totalTestsRun = 0;
	public int totalTestsPassed  = 0;
	public boolean runTest(TestCase tc){
		totalTestsRun++;
		String testOutput = getTestOutput(tc);
		String expectedCode;
		try {
			expectedCode = readFile(tc.expectedCodeFilePath, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Test file not found!");
			return false;
		}
		testOutput = testOutput.replaceAll("\n", "");
		
//		System.out.println("Expected Code:");
//		System.out.println(expectedCode);
//		System.out.println("Outputted Code:");
//		System.out.println(testOutput);
		boolean output = testOutput.equals(expectedCode);
		if(output)
			totalTestsPassed++;
		return output;
	}
	public String getTestOutput(TestCase tc){
		try {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    PrintStream ps = new PrintStream(baos);
		    PrintStream old = System.out;
		    System.setOut(ps);
			DesignParser.main(tc.classes);
		    System.out.flush();
		    System.setOut(old);
		    return baos.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public void writeTestOutputToFile(TestCase tc, String filePath){
		String output = getTestOutput(tc);
		try {
			FileWriter out = new FileWriter(filePath);
			out.write(output);
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Test output file not found!");
			return;
		} catch (IOException e) {
			System.out.println("IOException in test!");
			return;
		}
	}
	
	String readFile(String path, Charset encoding) throws IOException {
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  String output = new String(encoded, encoding);
	  return output.replaceAll("\n", "");
	}
	
	public void printTestStatus(){
		System.out.println("Tests passed: " + this.totalTestsPassed +"/" + this.totalTestsRun);
	}
}
