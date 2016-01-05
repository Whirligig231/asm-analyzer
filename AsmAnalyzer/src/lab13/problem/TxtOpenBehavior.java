package lab13.problem;

public class TxtOpenBehavior implements IFileOpenBehavior {

	@Override
	public String getCommand(String fname) {
		return "Notepad";
	}

}
