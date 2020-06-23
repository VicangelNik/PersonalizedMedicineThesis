package helpful_classes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// https://medium.com/@kevalpatel2106/how-to-make-the-perfect-singleton-de6b951dfdb0
/**
 * The Class AppLogger.
 */
public class AppLogger {

	/** The app logger. */
	private static volatile AppLogger appLogger;

	/** The logger. */
	private static Logger logger;

	/**
	 * Instantiates a new app logger.
	 */
	private AppLogger() {
		super();
		// Prevent form the reflection api.
		if (appLogger != null) {
			throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
		}
		// makeLogger("project.log");
		makeLogger("empca20_original_jrip.log");
	}

	/**
	 * Gets the single instance of AppLogger.
	 *
	 * @return single instance of AppLogger
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AppLogger getInstance() {
		if (appLogger == null) { // if there is no instance available... create new one
			synchronized (AppLogger.class) { // Check for the second time.
				// if there is no instance available... create new one
				if (appLogger == null) {
					try {
						Files.createDirectories(Paths.get(Constants.loggerPath));
					} catch (IOException e) {
						e.printStackTrace();
					}
					appLogger = new AppLogger();
				}
			}
		}
		return appLogger;
	}

	/**
	 * Make logger.
	 *
	 * @param title the title
	 */

	/**
	 * Instantiates a new app logger.
	 *
	 * @param title       the title
	 * @param fileToWrite the file to write
	 * @return
	 */
	private static void makeLogger(String title) {
		logger = Logger.getLogger(title);
		try {
			// In project.log will be logged everything on the project
			FileHandler fh = new FileHandler(Constants.loggerPath + title, false);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			// prevent to write to console output
			logger.setUseParentHandlers(false);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the logger.
	 *
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}
}
