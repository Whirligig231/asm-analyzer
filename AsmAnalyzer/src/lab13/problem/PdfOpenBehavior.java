package lab13.problem;

public class PdfOpenBehavior implements IFileOpenBehavior {

	@Override
	public String getCommand(String fname) {
		return "C:\\Program Files (x86)\\Adobe\\Reader 11.0\\Reader\\AcroRd32.exe";
	}

}
