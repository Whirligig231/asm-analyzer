package lab13.problem;

public class HtmlOpenBehavior implements IFileOpenBehavior {

	@Override
	public String getCommand(String fname) {
		return "explorer";
	}

}
