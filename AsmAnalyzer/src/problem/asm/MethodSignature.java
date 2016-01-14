package problem.asm;

public class MethodSignature {
	
	private String owner;
	private String name;
	private String desc;
	private int level;
	
	public MethodSignature(String owner, String name, String desc, int level) {
		super();
		this.owner = owner;
		this.name = name;
		this.desc = desc;
		this.level = level;
	}

	public String getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public int getLevel() {
		return level;
	}

}
