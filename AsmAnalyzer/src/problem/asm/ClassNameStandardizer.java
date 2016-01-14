package problem.asm;

public class ClassNameStandardizer {

	public static String standardize(String className) {
		if (className == null)
			return null;
		return className.replaceAll("[./$]", "_");
	}

}
