package lab13.problem;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppLaunchDriver {

	public static void main(String[] args) throws IOException, InterruptedException {
		// Register directory to the launcher
		Path dir = Paths.get("./input_output");
		AppLauncher launcher = new AppLauncher(dir);
		launcher.start();
		
		launcher.addFileObserver(new NewNamePrinterObserver());
		launcher.addFileObserver(new ModificationBackwardsPrintObserver());
		
		FileOpenObserver foo = new FileOpenObserver();
		foo.associate(".txt", new TxtOpenBehavior());
		IFileOpenBehavior html = new HtmlOpenBehavior();
		foo.associate(".html", html);
		foo.associate(".htm", html);
		foo.associate(".pdf", new PdfOpenBehavior());
		launcher.addFileObserver(foo);

		System.out.format("Launcher started watching %s ...%nPress the return key to stop ...%n", dir);

		// Wait for an input
		System.in.read();

		launcher.stopGracefully();
		launcher.join();

		System.out.println("Directory watching stopped ...");
	}

}
