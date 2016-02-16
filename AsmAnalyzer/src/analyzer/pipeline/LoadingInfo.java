package analyzer.pipeline;

public class LoadingInfo {
	
	private final float progress;
	private final String message;
	
	public LoadingInfo(float progress, String message) {
		super();
		this.progress = progress;
		this.message = message;
	}

	public float getProgress() {
		return progress;
	}

	public String getMessage() {
		return message;
	}

}
