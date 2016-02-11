package analyzer.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import analyzer.common.ClassNameStandardizer;

public class ClassNameStandardizerTest {

	@Test
	public void emptyTest() {
		String nullString = null;
		String nullStringExpectedOutput = null;
		String nullStringOutput = ClassNameStandardizer.forASM(nullString);
		assertEquals(nullStringExpectedOutput, nullStringOutput);
	}
	
	@Test
	public void alphaOnlyTest() {
		String testString = "foobar";
		String testStringExpectedOutput = "foobar";
		String testStringOutput = ClassNameStandardizer.forASM(testString);
		assertEquals(testStringExpectedOutput, testStringOutput);
	}
	
	@Test
	public void specialCharactersTest() {
		String testString = "foo./bar";
		String testStringExpectedOutput = "foo$bar";
		String testStringOutput = ClassNameStandardizer.forASM(testString);
		assertEquals(testStringExpectedOutput, testStringOutput);
	}

}
