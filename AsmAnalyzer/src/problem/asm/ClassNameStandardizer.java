package problem.asm;

public class ClassNameStandardizer {

	public static String standardize(String className) {
		return className.replaceAll("[./$]", "_");
	}

}
