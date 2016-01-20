package problem.asm;

import problem.asm.tests.Testable;

public class ClassNameStandardizer {

	public static String standardize(String className) {
		if (className == null)
			return null;
		return className.replaceAll("[$]", "__").replaceAll("[./]", "_");
	}

	public static String forASM(String className) {
		if (className == null)
			return null;
		return ClassNameStandardizer.standardize(className).replaceAll("__", "\\$").replaceAll("_", "/");
	}

}
