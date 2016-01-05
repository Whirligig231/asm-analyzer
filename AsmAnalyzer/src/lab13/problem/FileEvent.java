package lab13.problem;

public class FileEvent {
	
	private String type;
	private String fname;

	public FileEvent(String type, String fname) {
		super();
		this.type = type;
		this.fname = fname;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getFname() {
		return fname;
	}
	
	public void setFname(String fname) {
		this.fname = fname;
	}

}
