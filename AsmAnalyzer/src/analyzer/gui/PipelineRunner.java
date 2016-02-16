package analyzer.gui;

import java.io.IOException;

import analyzer.pipeline.IPipeline;

public class PipelineRunner implements Runnable {
	
	private IPipeline pipeline;
	private IErrorHandler handler;
	
	public PipelineRunner(IPipeline pipeline, IErrorHandler handler) {
		this.pipeline = pipeline;
		this.handler = handler;
	}

	@Override
	public void run() {
		try {
			this.pipeline.run();
		} catch (IllegalStateException | IOException | InterruptedException e) {
			this.handler.handleError(e.getMessage());
		}
	}

}
